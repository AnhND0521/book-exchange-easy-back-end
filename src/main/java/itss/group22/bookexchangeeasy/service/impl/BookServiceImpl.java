package itss.group22.bookexchangeeasy.service.impl;
import itss.group22.bookexchangeeasy.dto.BookDTO;
import itss.group22.bookexchangeeasy.dto.PostDTO;
import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.entity.Post;
import itss.group22.bookexchangeeasy.entity.StoreEvent;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import itss.group22.bookexchangeeasy.exception.ApiException;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.BookRepository;
import itss.group22.bookexchangeeasy.repository.PostRepository;
import itss.group22.bookexchangeeasy.repository.StoreEventRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import itss.group22.bookexchangeeasy.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final UserRepository userRepository;
    private final StoreEventRepository storeEventRepository;
    private final BookRepository bookRepository;
    private final PostRepository postRepository;
    private final ModelMapper mapper;
    @Override
    public void postBook(Long userId, PostDTO postDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Post post = mapper.map(postDTO, Post.class);
        post.setUser(user);
        post.setIsBookPost(true);
        Set<User> likedUsers = postDTO.getLikedUserIds().stream().map(id -> userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id))).collect(Collectors.toSet());
        post.setLikedUsers(likedUsers);
        StoreEvent  storeEvent  = storeEventRepository.findById(postDTO.getEventId()).orElseThrow(() -> new ResourceNotFoundException("StoreEvent", "id", postDTO.getEventId()));
        post.setEvent(storeEvent);
        Book book = mapper.map(postDTO.getBook(), Book.class);
        BookStatus bookStatus = BookStatus.valueOf(postDTO.getBook().getStatus());
        book.setStatus(bookStatus);
        book.setOwner(user);
        post.setBook(book);
        book.setPost(post);
        bookRepository.save(book);
    }

    @Override
    public void updateBook(Long userId, Long bookId, BookDTO bookDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));
        if (!book.getOwner().getId().equals(userId)) {
            throw new ApiException("You are not the owner of this book");
        }
        Book book1 = mapper.map(bookDTO, Book.class);
        book1.setId(bookId);
        book1.setOwner(user);
        book1.setPost(book.getPost());
        book1.setStatus(BookStatus.valueOf(bookDTO.getStatus()));
        bookRepository.save(book1);
    }

    @Override
    public void deleteBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));
        if (!book.getOwner().getId().equals(userId)) {
            throw new ApiException("You are not the owner of this book");
        }
        bookRepository.delete(book);
    }

    @Override
    public List<PostDTO> getAllBooks(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.getAllPost(BookStatus.AVAILABLE, pageable);

        return posts.stream().map(post -> {
           PostDTO postDTO =  mapper.map(post, PostDTO.class);
           postDTO.setUserId(post.getUser().getId());
              postDTO.setLikedUserIds(post.getLikedUsers().stream().map(User::getId).collect(Collectors.toSet()));
                postDTO.setEventId(post.getEvent().getId());
                BookDTO bookDTO = mapper.map(post.getBook(), BookDTO.class);
                bookDTO.setStatus(post.getBook().getStatus().name());

              postDTO.setBook(bookDTO);
            return postDTO;
        }).collect(Collectors.toList());

    }

}
