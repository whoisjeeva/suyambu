document.addEventListener('DOMNodeInsertedIntoDocument', onInsertedIntoDocument, true);
document.addEventListener('DOMNodeRemovedFromDocument', onRemovedFromDocument, true);
window.addEventListener('load', function(e) {
  setTimeout(function() {
    removeOnFocus(document.documentElement);
    document.removeEventListener('DOMNodeInsertedIntoDocument', onInsertedIntoDocument, true);
    document.removeEventListener('DOMNodeRemovedFromDocument', onRemovedFromDocument, true);
  }, 1);
}, false);


// Whenever an element is inserted into document, listen for
// simple event named 'focus'.
function onInsertedIntoDocument(e) {
  var elt = e.target;
  if (elt.nodeType === 1)
    elt.addEventListener('focus', onfocus, false);
}
function onRemovedFromDocument(e) {
  var elt = e.target;
  if (elt.nodeType === 1)
      removeOnFocus(elt);
}
function onfocus(e) {
  // In Chrome, caller is null if the user initiated the focus,
  // and non-null if the focus was caused by a call to element.focus().
  var causedByUser = (onfocus.caller == null);

  console.log('onfocus ' + e.target.nodeName +
      ': caused by user? ' +causedByUser +
      (e.target.autofocus ? ' autofocus' : ''));

  if (! causedByUser) {
    e.target.blur();
  }
}
// Clean up by removing all the 'focus' event listeners.
function removeOnFocus(elt) {
  elt.removeEventListener('focus', onfocus, false);
  for (var i = 0; i < elt.children.length; i++)
    removeOnFocus(elt.children[i]);
}