<main>
    <input id="login" type="hidden" name="login" value="<?php if(isset($_GET["iscr"])){echo $_GET["iscr"];} ?>" />
    <div class="container-fluid p-0 overflow-hidden">
        <header>
            <div class="row">
                <div class="mt-2 col-12 col-lg-6 d-flex justify-content-center d-lg-block justify-content-lg-start" id="iscrivitiImg">
                    <img src="../../img/sean.png" width=150 height=150 alt="Logo sito"/>
                </div>
                <div class="col-12 col-lg-6 d-flex justify-content-center d-lg-block justify-content-lg-start" id="iscrivitiP">
                    <p class="pt-10 w-75 text-center text-lg-start">
                        <span class="text-uppercase fs-3 fw-bold">Accedi ora</span><span> al portale degli avvistamenti</span>
                    </p>
                </div>
            </div>
            <div class="row">
                <div>
                    <h1 class="pt-3 text-center">ACCEDI</h1>
                </div>
            </div>
        </header>
        <div class="row mt-4 mb-5">
            <div class="col-1 col-md-3"></div>
            <div class="col-10 col-md-6 text-center">
                <div id="alert" role="alert"></div>
                <section class="card">
                    <h2 class="my-2">Inserisci credenziali</h2>
                    <div class="row">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-8">
                            <form id="form_login" autocomplete="on">
                                <div class="row mt-4 mb-2">
                                    <div class="col-md-12 col-lg-4 my-auto">
                                        <label for="email">Email: </label>
                                    </div>
                                    <div class="col-md-12 col-lg-8">
                                        <input type="email" id="email" name="email" placeholder="esempio@mail.it" required autofocus />
                                    </div>
                                </div>
                                <div class="row my-2">
                                    <div class="col-md-12 col-lg-4 my-auto">
                                        <label for="password">Password: </label>
                                    </div>
                                    <div class="col-md-12 col-lg-8">
                                        <input type="password" id="password" placeholder="password" name="pwd" required autocomplete="off" />
                                    </div>
                                </div>
                                <div class="row my-4">
                                    <div class="col-lg-12">
                                        <input type="submit" class="btn btn-1 btn-outline-primary" value="Accedi" />
                                    </div>
                                </div>
                            </form>
                            <div class="row my-4">
                                <div class="col-lg-12">
                                    <a class="btn btn-2 btn-sm btn-outline-secondary" href="signup.php">Non ho un account</a>
                                </div>
                            </div>
                            <div class="col-lg-2"></div>
                        </div>
                    </div>
                </section>
                <div class="col-1 col-md-3"></div>
            </div>
        </div>
    </div>
</main>