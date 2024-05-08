function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
      let c = ca[i];
      while (c.charAt(0) == ' ') {
        c = c.substring(1);
      }
      if (c.indexOf(name) == 0) {
        return c.substring(name.length, c.length);
      }
    }
    return "";
  }

function parseJwt(token) {
    // Split the token into three parts: header, payload, and signature
    const parts = token.split('.');

    // Decode the payload (which is the second part)
    const payload = JSON.parse(atob(parts[1]));

    return payload;
}