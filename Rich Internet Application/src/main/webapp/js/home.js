(function () {
    hideAll();
    document.getElementById("getSell").addEventListener("click", getSell, false);
    document.getElementById("getBuy").addEventListener("click", getBuy, false);
    let lastAction = localStorage.getItem('lastAction');
    if (lastAction == null || lastAction.value !== 'sell') getBuy();
    else getSell();
})();

function hideAll () {
    document.getElementById("sell").style.display = "none";
    document.getElementById("buy").style.display = "none"
    document.getElementById("buyDetails").style.display = "none";
    document.getElementById("sellDetails").style.display = "none";
}

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
    document.getElementById("sell").style.display = "block";
    document.getElementById("buy").style.display = "none"
    document.getElementById("buyDetails").style.display = "none";
    document.getElementById("sellDetails").style.display = "none";
    getSellData();
}

function getSellData () {
    const data = new FormData();
    const endpoint = "/sell?type=products";




    const request = new XMLHttpRequest();
    request.onreadystatechange = () => {
        if (request.readyState === 4) {
            if (request.status === 200)
            else alert("The signup failed");
        }
    };
    document.getElementById(id).addEventListener("submit", (e) => {
        e.preventDefault();
        if (!e.target.closest("form").reportValidity()) e.stopPropagation();
        else {
            const data = new FormData(e.target.closest("form"));
            request.open("post", endpoint, true);
            request.send(data);
        }
    });




}

function getBuy() {
    document.getElementById("sell").style.display = "none";
    document.getElementById("buy").style.display = "block"
    document.getElementById("buyDetails").style.display = "none";
    document.getElementById("sellDetails").style.display = "none";
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

}

function openDetails() {

}

function closeAuction() {

}

function makeOffer() {

}