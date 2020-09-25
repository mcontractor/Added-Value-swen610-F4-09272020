<!DOCTYPE html>
<head>
<#--    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>-->
<#--    <meta http-equiv="refresh" content="10">-->
    <title>Title</title>
    <link rel="stylesheet"  href="/css/_bootswatch.scss">
    <link rel="stylesheet"  href="/css/_variables.scss">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary" style="display:flex; justify-content:space-between">
    <a class="navbar-brand" href="">My Personal Learning Space (myPLS)</a>
    <ul class="navbar-nav">
        <li class="nav-item active">
            <#if pageType == "Login">
                <a class="nav-link" href="/register"> Register <span class="sr-only">(current)</span></a>
            <#else>
                <a class="nav-link" href="/login">Login <span class="sr-only">(current)</span></a>
            </#if>

        </li>
    </ul>
</nav>
<div class="card text-black border-primary mb-3" style="${styleVal}"!"max-width:32rem; margin-top:5%; left:30%">
<h2 class="card-header text-white bg-primary mb-3" style="padding:10pt">
    <#if pageType == "Login">
        Login
    <#else>
        Register
    </#if>
</h2>
<form style="padding:20pt" action="/verify-register" method="post">
    <fieldset>
        <#if pageType == "Register">
            <div style="display:flex; justify-content:space-between;">
                <div class="form-group">
                    <label for="firstName">First Name</label>
                    <input type="text" name="firstName" class="form-control" id="firstName" placeholder="First Name">
                </div>
                <div class="form-group">
                    <label for="lastName">Last Name</label>
                    <input type="text" name="lastName" class="form-control" id="lastName" placeholder="Last Name">
                </div>
            </div>
        </#if>
        <div class="form-group">
            <label for="email">Email address</label>
            <input type="email" class="form-control" name="email" id="email" aria-describedby="emailHelp" placeholder="rit@rit.edu">
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" name="pass" id="password" placeholder="Password">
            <#if pageType == "Login">
                <small id="forgotPassword" class="form-text" style="float:right;" href="">Forgot your password?</small>
            </#if>
        </div>
        <#if pageType == "Register">
            <div class="form-group">
                <label for="retypePassword">Retype Password</label>
                <input type="password" class="form-control" name="retPass" id="retypePassword" placeholder="Retype Password">
            </div>
        </#if>
        <div class="form-group" style="padding-top: 50pt;">
            <#if pageType == "Login">
                <small id="register" class="form-text" href="\register" style="float:left;">
                    <a href="\register" class="form-text"> Not a member? Register </a>
                </small>
            <#else>
                <small id="login" class="form-text" href="\login" style="float:left;">
                    <a href="\login" class="form-text"> Already a member? Login </a>
                </small>
            </#if>
            <button type="submit" class="btn btn-primary"  style="width:10rem; float:right;">
                Submit
            </button>
        </div>
    </fieldset>
</form>
</div>

</div>
</body>
</html>
