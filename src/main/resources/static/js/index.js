function toggleHide(elem) {
  elem = document.getElementById(elem);
  if (elem.hidden) {
    elem.hidden = false;
  } else {
    elem.value = "";
    elem.hidden = true;
  }
}
