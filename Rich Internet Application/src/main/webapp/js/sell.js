
window.onload = function () {
    pageOrchestrator = new PageOrchestrator();
    pageOrchestrator.start();
    pageOrchestrator.hideAll();
}

function PageOrchestrator() {
    this.sellPage = new SellPage();
    this.buyPage = new BuyPage();
    this.sellDetailspage = new SellDetailsPage();
    this.buyDetailspage = new BuyDetailPage();

    this.start = function () {

        //If no session is active returns to home
        if (!sessionStorage.getItem('userName')) {
            window.location.assign("authentication.html");
        }

        //Adds listener to the submit new product form
        document.getElementById("submitAddProduct").addEventListener("click", (e) => {
            e.preventDefault();
            if (!e.target.closest("form").reportValidity()) {
                alert("Data is invalid");
                return;
            }
            else {
                makeCall("POST", "/addProduct", this.sellPage.addProductForm,
                    addProductResponseHandler, true)
            }
        });
        document.getElementById("getSell").addEventListener("click", (e) => this.showSellPage());
        document.getElementById("getBuy").addEventListener("click", (e) => this.showBuyPage());
        document.getElementById("logout").addEventListener("click", (e) => this.logout())
    }

    this.logout = function () {
        makeCall("POST", "/logout", null, function (req) {
            if (req.readyState == 4) {
                switch (req.status) {
                    case (200) : {
                        sessionStorage.removeItem('userName')
                        window.location.assign("authentication.html");
                        break;
                    }
                    default :
                        alert("Unable to logout")
                }
            }
        })
    }

    this.fillSellPage = function () {
            makeCall("GET", "/getSell", null,
                fillSellPageHandler, false);
    }

    this.hideAll = function() {
        this.buyPage.hide();
        this.sellPage.hide();
        this.buyDetailspage.hide()
        this.sellDetailspage.hide();
    }

    this.showSellPage = function () {
        this.hideAll();
        this.sellPage.show();
        this.fillSellPage();
    }
    this.showBuyPage = function () {
        this.hideAll();
        this.buyPage.show();
    }
    this.showSellDetailsPage = function () {
        this.sellDetailspage.show();
    }
    this.showBuyDetailsPage = function () {
        this.buyDetailspage.show();
    }

}

function SellPage() {
    this.page = document.getElementById("sell");

    this.addProductForm = document.getElementById("addProductForm");
    this.myProducts = document.getElementById("myProducts");
    this.closedAuctions = document.getElementById("closedAuctions");
    this.activeAuctions = document.getElementById("activeAuctions");

    this.hide = function () {
        this.page.style.display = "none";
    }

    this.show = function () {
        this.page.style.display = "block";
    }

    this.fill = function (data) {
        let table = document.getElementById("myProducts")
        let myProductsArray = data.myProducts;
        if (myProductsArray && myProductsArray.length > 0) {
            document.getElementById("createNewAuctionDiv").style.display = "block";
            document.getElementById("noProductsDiv").style.display = "none";
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }
            myProductsArray.forEach(function (product) {
                let row, nameCell, description, priceCell, codeCell, tickCell;
                row = document.createElement("tr")

                codeCell = document.createElement("td");
                codeCell.textContent = product.productID;
                row.appendChild(codeCell);

                nameCell = document.createElement("td");
                nameCell.textContent = product.name;
                nameCell.title = product.description;
                row.appendChild(nameCell);

                priceCell = document.createElement("td");
                priceCell.textContent = product.price;
                row.appendChild(priceCell);

                tickCell = document.createElement("td");
                let tick = document.createElement("input");
                tick.setAttribute("type", "checkbox");
                tick.value = product.productID;
                tickCell.appendChild(tick);
                row.appendChild(tickCell);



                this.myProducts.appendChild(row);
            })
        }
        else {
            document.getElementById("noProductsDiv").style.display = "block";
            document.getElementById("createNewAuctionDiv").style.display = "none";
        }

        table = document.getElementById("activeAuctions")
        let myOpenAuctionsArray = data.myOpenAuctions;
        if (myOpenAuctionsArray && myOpenAuctionsArray) {
            document.getElementById("noAuctionClosedDiv").style.display = "none";
            document.getElementById("closedAuctionsDiv").style.display = "block";
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }
            myOpenAuctionsArray.forEach(function (auction) {
                let row, auctionID, price, days, detailsButton, time, name, productID;
                row = document.createElement("tr");

                auctionID = document.createElement("td");
                auctionID.textContent = auction.auctionID;
                row.appendChild(auctionID);

                price  = document.createElement("td");
                price.textContent = auction.price;
                row.appendChild(price);

                days = document.createElement("td");
                days.textContent = auction.remainingDays;
                row.appendChild(days);

                time  = document.createElement("td");
                time.textContent = auction.remainingHours;
                row.appendChild(time);

                detailsButton = document.createElement("td");
                let click = document.createElement("button");
                click.textContent = "Details";
                detailsButton.appendChild(click);
                row.appendChild(detailsButton);

                name  = document.createElement("td");
                name.textContent = auction.name;
                row.appendChild(name);

                productID  = document.createElement("td");
                productID.textContent = auction.productID;
                row.appendChild(productID);

                table.appendChild(row);
            })
        }
        else {
            document.getElementById("noAuctionClosedDiv").style.display = "block";
            document.getElementById("closedAuctionsDiv").style.display = "none";
        }

        table = document.getElementById("closedAuctions");
        let myClosedAuctionsArray = data.myClosedAuctions;
        if (myClosedAuctionsArray && myClosedAuctionsArray) {
            document.getElementById("noActiveAuctionsDiv").style.display = "none";
            document.getElementById("activeAuctionsDiv").style.display = "block";
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }
            myClosedAuctionsArray.forEach(function (auction) {
                let row, auctionID, price, detailsButton, name, productID;
                row = document.createElement("tr");

                auctionID = document.createElement("td");
                auctionID.textContent = auction.auctionID;
                row.appendChild(auctionID);

                price  = document.createElement("td");
                price.textContent = auction.price;
                row.appendChild(price);

                detailsButton = document.createElement("td");
                let click = document.createElement("button");
                click.textContent = "Details";
                detailsButton.appendChild(click);
                row.appendChild(detailsButton);

                name  = document.createElement("td");
                name.textContent = auction.name;
                row.appendChild(name);

                productID  = document.createElement("td");
                productID.textContent = auction.productID;
                row.appendChild(productID);

                table.appendChild(row);
            })
        }
        else {
            document.getElementById("noActiveAuctionsDiv").style.display = "block";
            document.getElementById("activeAuctionsDiv").style.display = "none";
        }
    }
}
function SellDetailsPage() {
    this.page = document.getElementById("sellDetails");

    this.hide = function () {
        this.page.style.display = "none";
    }

    this.show = function () {
        this.page.style.display = "block";
    }
}
function BuyPage() {
    this.page = document.getElementById("buy");

    this.hide = function () {
        this.page.style.display = "none";
    }

    this.show = function () {
        this.page.style.display = "block";
    }

}
function BuyDetailPage() {
    this.page = document.getElementById("buyDetails");

    this.hide = function () {
        this.page.style.display = "none";
    }
    this.show = function () {
        this.page.style.display = "block";
    }
}

function addProductResponseHandler(req) {
    if (req.readyState == 4) {
        const message = req.responseText;
        switch (req.status) {
            case (200) : {
                alert("Product successfully added");
                let orchestrator = new PageOrchestrator();
                pageOrchestrator.fillSellPage();
                break;
            }
            case (400) : {
                alert("Some data is not valid")
                break;
            }
            case (500) : {
                alert("The server encountered a problem")
                break;
            }
            default : {
                alert("Something went wrong")
                break;
            }
        }
    }
}
function fillSellPageHandler(req) {
    if (req.readyState == 4) {
        switch (req.status) {
            case (200) : {
                const data = JSON.parse((req.responseText));
                if (data) {
                    let sellPage = new SellPage()
                    sellPage.fill(data);
                }
                else {

                }
                break;
            }
            case (500) : {
                alert("Server error: could not load Sell page");
            }
        }
    }
}


function makeCall(method, url, formElement, callback, reset) {
    const req = new XMLHttpRequest();
    req.onreadystatechange = function() {
        callback(req) };

    req.open(method, url);

    if (formElement == null) {
        req.send();
    }
    else {
        req.send(new FormData(formElement));
        console.log("sending...")
        if (reset) {
            formElement.reset();
        }
    }
}