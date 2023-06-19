(
    function () {
        var pageOrchestrator = new PageOrchestrator();
        pageOrchestrator.start();
        pageOrchestrator.hideAll();
        pageOrchestrator.showSellPage();
    } ()
)

function PageOrchestrator() {

    this.sellPage = new SellPage();
    this.buyPage = new BuyPage();
    this.sellDetailspage = new SellDetailsPage();
    this.buyDetailspage = new BuyDetailPage();

    this.start = function () {
        //Adds listener to the submit new product form
        this.sellPage.addProductForm.addEventListener("submit", (e) => {
            if (!e.target.closest("form").reportValidity()) e.stopPropagation();
            else {
                makeCall("POST", "/addProduct", this.sellPage.addProductForm,
                    addProductResponseHandler, false)
            }
        }
        )
    }

    this.hideAll = function() {
        this.buyPage.hide();
        this.sellPage.hide();
        this.buyDetailspage.hide()
        this.sellDetailspage.hide();
    }


    this.showSellPage = function () {
        this.sellPage.show();
    }
    this.showBuyPage = function () {
        this.sellPage.show();
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