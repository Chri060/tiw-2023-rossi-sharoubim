(function () {
    document.getElementById("sell").style.display = "block";
    document.getElementById("buy").style.display = "none"
    document.getElementById("buyDetails").style.display = "none";
    document.getElementById("sellDetails").style.display = "none";
})();

function logout () {
    const endpoint = "/logout";
    const request = new XMLHttpRequest();
    request.open("post", endpoint, true);
    request.send();
    request.onreadystatechange = () => {
        if (request.readyState === 4) {
            if (request.status === 200) window.location.assign("authentication.html");
        }
    };
}

function getSell() {

}

function getBuy() {

}

function searchProduct() {

}

function sellDetails() {

}

function addProduct() {

}

function createAuction() {

}

function closedDetails() {
    document.getElementById("sell").style.display = "none";
    document.getElementById("buy").style.display = "none"
    document.getElementById("buyDetails").style.display = "none";
    document.getElementById("sellDetails").style.display = "block";
}

function openDetails() {
    document.getElementById("sell").style.display = "none";
    document.getElementById("buy").style.display = "none"
    document.getElementById("buyDetails").style.display = "none";
    document.getElementById("sellDetails").style.display = "block";
}

function closeAuction() {

}

function makeOffer() {

}