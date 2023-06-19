/* Generic function create to handle any request
    method = "GET" or "POST"
    url = URL to send the request to
    formElement = form to send in body (if apply, null otherwise)
    cback = callback to invoke when status change to handle responses from server
    reset = if we use formElement then if we reset it or not
*/
function makeCall(method, url, formElement, callback, reset) {
    const req = new XMLHttpRequest();
    req.onreadystatechange = function() { callback(req) };

    req.open(method, url);

    if (formElement == null) { req.send(); }
    else {
        req.send(new FormData(formElement));
        console.log("sending...")
        if (reset) { formElement.reset(); }
    }
}