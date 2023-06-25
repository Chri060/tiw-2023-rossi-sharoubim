{
    window.onload = function () {
        pageOrchestrator = new PageOrchestrator();
        pageOrchestrator.start();
        pageOrchestrator.hideAll();

        let lastAction = Cookies.get(sessionStorage.getItem('userName') + "LastActionCookie");
        switch (lastAction) {
            case ("sell") : {
                pageOrchestrator.showSellPage();
                break;
            }
            default : {
                pageOrchestrator.showBuyPage();
                break;
            }
        }
    }

    function PageOrchestrator() {
        this.sellPage = new SellPage();
        this.buyPage = new BuyPage();
        this.sellDetailspage = new SellDetailsPage();
        this.buyDetailspage = new BuyDetailPage();

        this.start = function () {
            //Adds listener to the submit new product form
            document.getElementById("submitAddProduct").addEventListener("click", (e) => {
                e.preventDefault();
                if (!e.target.closest("form").reportValidity()) {
                    alert("Data is invalid");
                    return;
                } else {
                    makeCall("POST", "/addProduct", this.sellPage.addProductForm,
                        addProductResponseHandler, true)
                }
            });
            //Adds listener to load sellPage
            document.getElementById("getSell").addEventListener("click", (e) => this.showSellPage());
            //Adds listener to load buyPage
            document.getElementById("getBuy").addEventListener("click", (e) => this.showBuyPage());
            //Adds listener to logout
            document.getElementById("logout").addEventListener("click", (e) => this.logout());
            //Adds listener to create new auction button
            document.getElementById("submitNewAuction").addEventListener("click", (e) => {
                e.preventDefault();
                if (!e.target.closest("form").reportValidity()) {
                    alert("Data is invalid");
                    return;
                } else {
                    makeCall("POST", "/doCreateAuction", this.sellPage.createAuctionForm,
                        addProductResponseHandler, true)
                }
            });
            //Adds listener to search button
            document.getElementById("submitSearch").addEventListener("click", (e) => {
                let search = document.getElementById("searchInput").value;
                if (search === "") {
                    alert("Write something please");
                    return;
                }
                makeCall("GET", "/getMatchingAuctions?article=" + search, null,
                    fillSearchAuctionHandler, false);
            })
            //Updates cookies
            document.getElementById("submitNewAuction").addEventListener("click", (e) => {
                if (e.target.closest("form").reportValidity()) {
                    Cookies.set(sessionStorage.getItem('userName') + 'LastActionCookie', 'sell', {expires: 30});
                }
            })
            //Adds close auction listener
            document.getElementById("closeAuctionButton").addEventListener("click", (e) => {
                e.preventDefault();
                makeCall("POST", "/doClose?auctionID=" + e.target.value, null,
                    closeAuctionResponseHandler, false)
            });
            //Adds new offerListener
            document.getElementById("submitNewOffer").addEventListener("click", (e) => {
                e.preventDefault();
                if (!e.target.closest("form").reportValidity()) {
                    alert("Data is invalid");
                    return;
                } else {
                    makeCall("POST", "/doOffer", document.getElementById("offerForm"),
                        doOfferResponseHandler, true);
                }
            })
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

        this.fillBuyPage = function () {
            makeCall("GET", "/getWonAuctions", null,
                fillWonActionHandler, false);

            const auctionsArray = getSeenAuctionsID();
            let params = "";
            for (const auctionsArrayElement of auctionsArray) {
                params = params + `auctionsIds=${auctionsArrayElement}&`
            }
            if (params.length > 0)
                params = params.slice(0, params.length - 1)

            makeCall("GET", `/getSeenAuction?${params}`, null,
                fillSeenActionHandler, false);

            document.getElementById("searchResultTable").style.display = "none";
        }

        this.fillSellDetailsPage = function (auctionID) {
            makeCall("GET", "/getAuctionDetails?auctionID=" + auctionID, null,
                fillSellDetailPageHandler, false);
        }

        this.fillBuyDetailsPage = function (auctionID) {
            makeCall("GET", "/getSearchedAuctionDetails?auctionID=" + auctionID, null,
                fillBuyDetailsPageHandler, false);
        }

        this.hideAll = function () {
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
            this.fillBuyPage();
        }

        this.showSellDetailsPage = function () {
            this.hideAll();
            this.sellDetailspage.show();
        }

        this.showBuyDetailsPage = function () {
            this.hideAll();
            this.buyDetailspage.show();
        }
    }

    function SellPage() {
        this.page = document.getElementById("sell");
        this.addProductForm = document.getElementById("addProductForm");
        this.myProducts = document.getElementById("myProducts");
        this.closedAuctions = document.getElementById("closedAuctions");
        this.activeAuctions = document.getElementById("activeAuctions");
        this.createAuctionForm = document.getElementById("createAuctionForm");

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
                    tick.setAttribute("name", "product");
                    tick.value = product.productID;
                    tickCell.appendChild(tick);
                    row.appendChild(tickCell);

                    this.myProducts.appendChild(row);
                })
            } else {
                document.getElementById("noProductsDiv").style.display = "block";
                document.getElementById("createNewAuctionDiv").style.display = "none";
            }

            table = document.getElementById("activeAuctions")
            let myOpenAuctionsArray = data.myOpenAuctions;
            if (myOpenAuctionsArray && myOpenAuctionsArray.length > 0) {
                document.getElementById("noActiveAuctionsDiv").style.display = "none";
                document.getElementById("activeAuctionsDiv").style.display = "block";
                while (table.rows.length > 1) {
                    table.deleteRow(1);
                }
                myOpenAuctionsArray.forEach(function (auction) {
                    let row, auctionID, price, days, detailsButton, time, productList;
                    row = document.createElement("tr");

                    auctionID = document.createElement("td");
                    auctionID.textContent = auction.auctionID;
                    row.appendChild(auctionID);

                    price = document.createElement("td");
                    price.textContent = auction.price;
                    row.appendChild(price);

                    days = document.createElement("td");
                    days.textContent = auction.remainingDays;
                    row.appendChild(days);

                    time = document.createElement("td");
                    time.textContent = auction.remainingHours;
                    row.appendChild(time);

                    detailsButton = document.createElement("td");
                    let click = document.createElement("button");
                    click.textContent = "Details";
                    click.setAttribute("value", auction.auctionID);
                    detailsButton.appendChild(click);
                    row.appendChild(detailsButton);

                    //Adds listener to details button
                    click.addEventListener("click", (e) => {
                        let auctionID = e.target.value;
                        pageOrchestrator.fillSellDetailsPage(auctionID);
                    })

                    productList = document.createElement("td");
                    let temp = "";
                    auction.productList.forEach(function (product) {
                            temp += product.name;
                            temp += "</br>";
                        }
                    )
                    productList.innerHTML = temp;
                    row.appendChild(productList);
                    table.appendChild(row);
                })
            } else {
                document.getElementById("noActiveAuctionsDiv").style.display = "block";
                document.getElementById("activeAuctionsDiv").style.display = "none";
            }

            table = document.getElementById("closedAuctions");
            let myClosedAuctionsArray = data.myClosedAuctions;
            if (myClosedAuctionsArray && myClosedAuctionsArray.length > 0) {
                document.getElementById("noAuctionClosedDiv").style.display = "none";
                document.getElementById("closedAuctionsDiv").style.display = "block";
                while (table.rows.length > 1) {
                    table.deleteRow(1);
                }
                myClosedAuctionsArray.forEach(function (auction) {
                    let row, auctionID, price, detailsButton, productList;
                    row = document.createElement("tr");

                    auctionID = document.createElement("td");
                    auctionID.textContent = auction.auctionID;
                    row.appendChild(auctionID);

                    price = document.createElement("td");
                    price.textContent = auction.price;
                    row.appendChild(price);

                    detailsButton = document.createElement("td");
                    let click = document.createElement("button");
                    click.textContent = "Details";
                    click.setAttribute("value", auction.auctionID);
                    detailsButton.appendChild(click);
                    row.appendChild(detailsButton);

                    //Adds listener to details button
                    click.addEventListener("click", (e) => {
                        let auctionID = e.target.value;
                        pageOrchestrator.fillSellDetailsPage(auctionID);

                    })

                    productList = document.createElement("td");
                    let temp = "";
                    auction.productList.forEach(function (product) {
                            temp += product.name;
                            temp += "</br>";
                        }
                    )
                    productList.innerHTML = temp;
                    row.appendChild(productList);

                    table.appendChild(row);
                })
            } else {
                document.getElementById("noAuctionClosedDiv").style.display = "block";
                document.getElementById("closedAuctionsDiv").style.display = "none";
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

        this.fill = function (data) {
            let table = document.getElementById("auctionDetails");
            let productList = data.productList;
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }
            if (data.active) {
                let closeButton = document.getElementById("closeAuctionButton");
                closeButton.style.display = "block";
                closeButton.setAttribute("value", productList[0].auctionID);
            } else {
                document.getElementById("closeAuctionButton").style.display = "none";
            }
            productList.forEach(function (auction) {
                let row, codeCell, priceCell, productID, productName, productDescription, image;
                row = document.createElement("tr")

                codeCell = document.createElement("td");
                codeCell.textContent = auction.auctionID;
                row.appendChild(codeCell);

                priceCell = document.createElement("td");
                priceCell.textContent = auction.price;
                row.appendChild(priceCell);

                productID = document.createElement("td");
                productID.textContent = auction.productID;
                row.appendChild(productID);

                productName = document.createElement("td");
                productName.textContent = auction.name;
                row.appendChild(productName);

                productDescription = document.createElement("td");
                productDescription.textContent = auction.description;
                row.appendChild(productDescription);

                image = document.createElement("img");
                image.setAttribute("alt", "");
                image.setAttribute("src", auction.image);
                let imagetd = document.createElement("td");
                imagetd.appendChild(image);
                row.appendChild(imagetd);

                table.appendChild(row);
            })

            table = document.getElementById("offerList");
            let offersList = data.offersList;
            if (offersList && offersList.length > 0) {
                table.style.display = "block";
                document.getElementById("noOfferList").style.display = "none";
                while (table.rows.length > 1) {
                    table.deleteRow(1);
                }
                offersList.forEach(function (offer) {
                    let row, userName, offering, date;
                    row = document.createElement("tr");

                    userName = document.createElement("td");
                    userName.textContent = offer.userName;
                    row.appendChild(userName);

                    offering = document.createElement("td");
                    offering.textContent = offer.offering;
                    row.appendChild(offering);

                    date = document.createElement("td");
                    date.textContent = offer.date;
                    row.appendChild(date);

                    table.appendChild(row);
                })
            } else {
                document.getElementById("noOfferList").style.display = "block";
                table.style.display = "none";
            }

            table = document.getElementById("winnerTable")
            let winner = data.winner;
            if (winner) {
                while (table.rows.length > 1) {
                    table.deleteRow(1);
                }
                let userID, username, email, city, address, province, row;

                row = document.createElement("tr");

                userID = document.createElement("td");
                userID.textContent = winner.userID;
                row.appendChild(userID);

                username = document.createElement("td");
                username.textContent = winner.username
                row.appendChild(username);

                email = document.createElement("td");
                email.textContent = winner.email
                row.appendChild(email);

                city = document.createElement("td");
                city.textContent = winner.city;
                row.appendChild(city);

                address = document.createElement("td");
                address.textContent = winner.address
                row.appendChild(address);

                province = document.createElement("td");
                province.textContent = winner.province
                row.appendChild(province);

                table.appendChild(row);
                table.style.display = "block";
            } else {
                table.style.display = "none";
            }
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

        this.fillWonAuctions = function (data) {
            if (data.length > 0) {
                let table = document.getElementById("wonAuctionsTable");
                table.style.display = "block";
                document.getElementById("noWonAuctions").style.display = "none";
                while (table.rows.length > 1) {
                    table.deleteRow(1);
                }
                data.forEach(function (auction) {
                    let row, auctionID, detailsButton, productList, maxOffer, productDiv;

                    row = document.createElement("tr");

                    auctionID = document.createElement("td");
                    auctionID.textContent = auction.auctionID;
                    row.appendChild(auctionID);

                    maxOffer = document.createElement("td");
                    maxOffer.textContent = auction.maxOffer;
                    row.appendChild(maxOffer);

                    detailsButton = document.createElement("td");
                    let click = document.createElement("button");
                    click.textContent = "Details";
                    click.setAttribute("value", auction.auctionID);
                    detailsButton.appendChild(click);
                    row.appendChild(detailsButton);

                    click.addEventListener("click", (e) => {
                        let auctionID = e.target.value;
                        pageOrchestrator.fillBuyDetailsPage(auctionID);
                    })

                    productList = document.createElement("td");
                    let temp = "";
                    auction.productList.forEach(function (product) {
                            temp += product.name;
                            temp += "</br>";
                        }
                    )
                    productList.innerHTML = temp;
                    row.appendChild(productList);

                    table.appendChild(row);
                })
            } else {
                document.getElementById("wonAuctionsTable").style.display = "none";
                document.getElementById("noWonAuctions").style.display = "block";
            }
        }





        this.fillSeenAuctions = function (data) {
            let table = document.getElementById("seenAuctionTable");
            if (data.length == 0) {
                document.getElementById("seenAuctionDiv").style.display = "none";
                return;
            }
            document.getElementById("seenAuctionDiv").style.display = "block";
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }

            data.forEach(function (auction) {
                let row = document.createElement("tr");

                let auctionID, maxOffer, details, productList;
                auctionID = document.createElement("td");
                auctionID.textContent = auction.auctionID;
                row.appendChild(auctionID);

                maxOffer = document.createElement("td");
                maxOffer.textContent = Math.max(auction.maxOffer, auction.price);
                row.appendChild(maxOffer);


                details = document.createElement("td");
                let click = document.createElement("button");
                click.textContent = "Details";
                click.setAttribute("value", auction.auctionID);

                click.addEventListener("click", (e) => {
                    Cookies.set(sessionStorage.getItem('userName') + 'LastActionCookie', 'buy', {expires: 30});
                    let auctionID = e.target.value;
                    Cookies.set(sessionStorage.getItem('userName') + 'SeenAuction' + auctionID, auctionID, {expires: 30});
                    pageOrchestrator.fillBuyDetailsPage(auctionID);
                })

                details.appendChild(click);
                row.appendChild(details);

                productList = document.createElement("td");
                var temp = "";
                auction.productList.forEach(function (product) {
                        temp += product.name;
                        temp += "</br>";
                    }
                )
                productList.innerHTML = temp;
                row.appendChild(productList);

                table.appendChild(row);
            })
        }

        this.fillMatchingAuctions = function (data) {
            let table = document.getElementById("searchResultTable");
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }
            if (data.length > 0) {
                table.style.display = "block";
            } else {
                table.style.display = "none";
            }
            data.forEach(function (auction) {
                let row = document.createElement("tr");
                let auctionID, price, remainingDays, remainingHours, details, productList;
                auctionID = document.createElement("td");
                auctionID.textContent = auction.auctionID;
                row.appendChild(auctionID);

                price = document.createElement("td");
                price.textContent = Math.max(auction.maxOffer, auction.price);
                row.appendChild(price);

                remainingDays = document.createElement("td");
                remainingDays.textContent = auction.remainingDays;
                row.appendChild(remainingDays);

                remainingHours = document.createElement("td");
                remainingHours.textContent = auction.remainingHours;
                row.appendChild(remainingHours);

                details = document.createElement("td");
                let click = document.createElement("button");
                click.textContent = "Details";
                click.setAttribute("value", auction.auctionID);

                click.addEventListener("click", (e) => {
                    Cookies.set(sessionStorage.getItem('userName') + 'LastActionCookie', 'buy', {expires: 30});
                    let auctionID = e.target.value;
                    Cookies.set(sessionStorage.getItem('userName') + 'SeenAuction' + auctionID, auctionID, { expires: 30 });
                    pageOrchestrator.fillBuyDetailsPage(auctionID);
                })

                details.appendChild(click);
                row.appendChild(details);

                productList = document.createElement("td");
                var temp = "";
                auction.productList.forEach(function (product) {
                        temp += product.name;
                        temp += "</br>";
                    }
                )
                productList.innerHTML = temp;
                row.appendChild(productList);

                table.appendChild(row);
            })
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

        this.fill = function (data) {
            let table = document.getElementById("auctionDetails1");
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }

            data.productList.forEach(function (product) {
                let row, productId, name, description, image;
                row = document.createElement("tr");

                productId = document.createElement("td");
                productId.textContent = product.productID;
                row.appendChild(productId);
                name = document.createElement("td");
                name.textContent = product.name;
                row.appendChild(name)

                description = document.createElement("td");
                description.textContent = product.description;
                row.appendChild(description);

                image = document.createElement("img");
                image.setAttribute("alt", "");
                image.setAttribute("src", product.image);
                let imagetd = document.createElement("td");
                imagetd.appendChild(image);
                row.appendChild(imagetd);

                table.appendChild(row);
            })

            table = document.getElementById("offersTable");
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }

            if (data.offersList.length == 0) {
                document.getElementById("noOffers").style.display = "block";
                document.getElementById("offersTable").style.display = "none";
            }
            else {
                document.getElementById("noOffers").style.display = "none";
                document.getElementById("offersTable").style.display = "block";
                data.offersList.forEach(function (offers) {

                    let row, username, offering, date;
                    row = document.createElement("tr");

                    username = document.createElement("td");
                    username.textContent = offers.userName;
                    row.appendChild(username);

                    offering = document.createElement("td");
                    offering.textContent = offers.offering;
                    row.appendChild(offering);

                    date = document.createElement("td");
                    date.textContent = offers.date;
                    row.appendChild(date);

                    table.appendChild(row);
                })
            }

            document.getElementById("startingPrice").textContent = data.price;
            document.getElementById("minimumRise").textContent = data.rise;

            if (data.active) {
                document.getElementById("newOfferFieldset").style.display = "block";
                document.getElementById("auctionIDOfferForm").setAttribute("value", data.productList[0].auctionID)
            } else {
                document.getElementById("newOfferFieldset").style.display = "none";
            }
        }
    }

    function addProductResponseHandler(req) {
        if (req.readyState == 4) {
            const message = req.responseText;
            switch (req.status) {
                case (200) : {
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
                    break;
                }
                case (500) : {
                    alert("Server error: could not load Sell page");
                }
            }
        }
    }

    function fillWonActionHandler(req) {
        if (req.readyState == 4) {
            switch (req.status) {
                case (200) : {
                    const data = JSON.parse((req.responseText));
                    if (data) {
                        let buyPage = new BuyPage()
                        buyPage.fillWonAuctions(data);
                    }
                    break;
                }
                case (500) : {
                    alert("Server error: could not load won auctions");
                }
            }
        }
    }

    function fillSeenActionHandler(req) {
        if (req.readyState == 4) {
            switch (req.status) {
                case (200) : {
                    const data = JSON.parse((req.responseText));
                    if (data) {
                        let buyPage = new BuyPage()
                        buyPage.fillSeenAuctions(data);
                    }
                    break;
                }
                case (400) : {
                    document.getElementById("seenAuctionDiv").style.display = "none";
                    break;
                }
                case (500) : {
                    alert("Server error: could not load seen auctions");
                }
            }
        }
    }

    function fillSearchAuctionHandler(req) {
        if (req.readyState == 4) {
            switch (req.status) {
                case (200) : {
                    const data = JSON.parse((req.responseText));
                    if (data) {
                        let buyPage = new BuyPage()
                        buyPage.fillMatchingAuctions(data);
                    }
                    break;
                }
                case (400) : {
                    alert("Told you to write something");
                    break;
                }
                case (500) : {
                    alert("Server error: could not load searched auctions");
                }
            }
        }
    }

    function fillSellDetailPageHandler(req) {
        if (req.readyState == 4) {
            switch (req.status) {
                case (200) : {
                    const data = JSON.parse((req.responseText));
                    if (data) {
                        let sellDetailsPage = new SellDetailsPage()
                        sellDetailsPage.fill(data);
                        pageOrchestrator.hideAll();
                        pageOrchestrator.showSellDetailsPage();
                    } else {
                        alert("Data from server is not valid")
                    }
                    break;
                }
                case (400) : {
                    alert("Bad request");
                    break;
                }
                case (403) :
                    alert("This auction is already closed");
                    break;
                case (500) : {
                    alert("Server error: could not load sell details page");
                    break;
                }
            }
        }
    }

    function fillBuyDetailsPageHandler(req) {
        if (req.readyState == 4) {
            switch (req.status) {
                case (200) : {
                    const data = JSON.parse((req.responseText));
                    if (data) {
                        pageOrchestrator.showBuyDetailsPage();
                        let buyDetailPage = new BuyDetailPage()
                        buyDetailPage.fill(data);
                    } else {
                        alert("Data from server is not valid")
                    }
                    break;
                }
                case (400) : {
                    alert("Bad request");
                    break;
                }
                case (403) :
                    alert("This auction is already closed");
                    break;
                case (500) : {
                    alert("Server error: could not load Sell page");
                    break;
                }
            }
        }
    }

    function doOfferResponseHandler(req) {
        if (req.readyState == 4) {
            switch (req.status) {
                case (200) : {
                    const data = JSON.parse((req.responseText));
                    if (data) {
                        pageOrchestrator.showBuyDetailsPage();
                        let buyDetailPage = new BuyDetailPage()
                        buyDetailPage.fill(data);
                    } else {
                        alert("Data from server is not valid")
                    }
                    break;
                }
                case (400) : {
                    alert("Bad request");
                    break;
                }
                case (403) : {
                    alert("You are the owner of this auction");
                    break;
                }
                case (406) : {
                    alert("Offer not valid");
                    break;
                }
                case (500) : {
                    alert("Server error: could not load Sell page");
                    break;
                }
            }
        }
    }

    function closeAuctionResponseHandler(req) {
        if (req.readyState == 4) {
            switch (req.status) {
                case (200) : {
                    document.getElementById("closeAuctionButton").style.display = "none";
                    fillSellDetailPageHandler(req);
                    break;
                }
                case (400) : {
                    alert("Bad request");
                    break;
                }
                case (403) :
                    alert("You are not the owner of this auction");
                    break;
                case (500) : {
                    alert("Server error: could not load Sell page");
                    break;
                }
            }
        }
    }

    function makeCall(method, url, formElement, callback, reset) {
        const req = new XMLHttpRequest();
        req.onreadystatechange = function () {
            callback(req)
        };

        req.open(method, url);

        if (formElement == null) {
            req.send();
        } else {
            let x = new FormData(formElement);
            req.send(x);
            console.log("sending...")
            if (reset) {
                formElement.reset();
            }
        }
    }

    function getSeenAuctionsID() {
        let cookieMap = Cookies.get();
        let idArray = [];
        for (const cookieMapKey in cookieMap) {
            if (cookieMapKey.includes(sessionStorage.getItem('userName') + 'SeenAuction'))
                idArray.push(cookieMap[cookieMapKey])
        }
        return idArray;
    }
}