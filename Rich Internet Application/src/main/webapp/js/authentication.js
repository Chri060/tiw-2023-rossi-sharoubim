(function () {
    //hides signup form
    { document.getElementById("signUpDiv").style.display = "none"; }

    //login
    {   const id = "sign-in-form";
        const endpoint = "/login";
        const request = new XMLHttpRequest();
        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                if (request.status === 200) window.location.assign("home.html");
                else alert("The sign in failed");
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

    //signup
    {   const id = "sign-up-form";
        const endpoint = "/register";
        const request = new XMLHttpRequest();
        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                if (request.status === 200) window.location.assign("home.html");
                else alert("The signup failed");
            }
        };
        document.getElementById(id).addEventListener("submit", (e) => {
            e.preventDefault();
            if (!e.target.closest("form").reportValidity()) e.stopPropagation();
            else {
                const data = new FormData(e.target.closest("form"));
                data.delete("pass-confirm");
                request.open("post", endpoint, true);
                request.send(data);
            }
        });
    }

    //input checking
    {
        const signInUsername = document.getElementById("signInUsername");
        const signInPassword = document.getElementById("signInPassword");
        const username = document.getElementById("username");
        const email = document.getElementById("email");
        const city = document.getElementById("city");
        const province = document.getElementById("province");
        const address = document.getElementById("address");
        const password = document.getElementById("password");
        const password1 = document.getElementById("password1");

        signInUsername.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1) elem.setCustomValidity("Field is required");
            else if (value.length > 31) elem.setCustomValidity("The username inserted is too long");
            else if (!value.match("^[a-zA-Z0-9_-]{4,16}$")) elem.setCustomValidity("The username inserted is not valid");
            else elem.setCustomValidity("");
        };

        signInPassword.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1) elem.setCustomValidity("Field is required");
            else if (value.length > 63) elem.setCustomValidity("The password inserted is too long");
            //else if (!value.match("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,16}$")) elem.setCustomValidity("The password inserted is not valid");
            else elem.setCustomValidity("");
        };

        username.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1) elem.setCustomValidity("Field is required");
            else if (value.length > 31) elem.setCustomValidity("The username inserted is too long");
            else if (!value.match("^[a-zA-Z0-9_-]{4,16}$"))  elem.setCustomValidity("The username inserted is not valid");
            else elem.setCustomValidity("");
        };

        email.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1) elem.setCustomValidity("Field is required");
            else if (value.length > 255) elem.setCustomValidity("The e-mail inserted is too long");
            else if (!value.match("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+"))  elem.setCustomValidity("E-mail address not valid");
            else elem.setCustomValidity("");
        };

        city.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1) elem.setCustomValidity("Field is required");
            else if (value.length > 31) elem.setCustomValidity("The city inserted is too long");
            else if (!value.match("^[a-zA-Z]{1,32}$"))  elem.setCustomValidity("City not valid");
            else elem.setCustomValidity("");
        };

        province.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1) elem.setCustomValidity("Field is required");
            else if (value.length > 31) elem.setCustomValidity("The province inserted is too long");
            else if (!value.match("^[a-zA-Z]{1,32}$"))  elem.setCustomValidity("Province not valid");
            else elem.setCustomValidity("");
        };

        province.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1) elem.setCustomValidity("Field is required");
            else if (value.length > 63) elem.setCustomValidity("The address inserted is too long");
            else if (!value.match("^[a-zA-Z0-9,]{1,64}$"))  elem.setCustomValidity("Address not valid");
            else elem.setCustomValidity("");
        };

        password.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1) elem.setCustomValidity("Field is required");
            else if (value.length > 64) elem.setCustomValidity("The password inserted is too long");
            else if (!value.match("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,16}$")) elem.setCustomValidity("The password inserted is not valid");
            else elem.setCustomValidity("");
        };

        password1.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1) elem.setCustomValidity("Field is required");
            else if (value.length > 64) elem.setCustomValidity("The password inserted is too long");
            else if (!value.match("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,16}$")) elem.setCustomValidity("The password inserted is not valid");
            else elem.setCustomValidity("");
        };
    }
})();

function hideAndShow() {
    let signIn = document.getElementById("signInDiv");
    let signUp = document.getElementById("signUpDiv");
    if (signIn.style.display === "none") {
        signIn.style.display = "block";
    } else {
        signIn.style.display = "none";
    }
    if (signUp.style.display === "none") {
        signUp.style.display = "block";
    } else {
        signUp.style.display = "none";
    }
}