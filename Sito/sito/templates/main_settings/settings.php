<main class="big-margin">
    <header>
    <h1 class="text-center">Impostazioni</h1>
    </header>
    <div class="container h-100">
        <div id="settings" class="row gutters-sm">
            <aside class="col-md-4 d-none d-md-block">
                <div class="card">
                    <div class="card-body btnmenu">
                        <nav class="nav flex-column nav-pills nav-gap-y-1">
                            <button class="nav-item nav-link nav-1 active" data-type="profile">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-user me-2">
                                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                                    <circle cx="12" cy="7" r="4"></circle>
                                </svg>Informazioni profilo
                            </button>
                            <button class="nav-item nav-link nav-1" data-type="security">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-shield me-2">
                                    <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
                                </svg>Sicurezza
                            </button>
                        </nav>
                    </div>
                </div>
            </aside>
            <div class="col-md-8">
                <div class="card">
                    <header class="card-header border-bottom mb-3 d-flex d-md-none btnmenu">
                        <nav class="nav flex-row nav-pills nav-gap-y-1">
                                <button class="nav-item nav-link nav-1 nav-horizontal active" data-type="profile">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-user">
                                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                                        <circle cx="12" cy="7" r="4"></circle>
                                    </svg>
                                </button>
                                <button class="nav-item nav-link nav-1 nav-horizontal" data-type="security">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-shield">
                                        <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
                                    </svg>
                                </button>
                            </nav>
                    </header>
                    <section class="card-body">
                        <div id="alert" class="my-2" role="alert"></div>
                        <div id="profile" class="section">
                            <header class="my-2">
                                <h2>Informazioni profilo</h2>
                                <hr>
                            </header>
                            <form id="frmProfile" class="mb-3">
                                <div class="form-group">
                                    <div class="row my-2">
                                        <div class="col-md-12 col-lg-4 my-auto">
                                            <label for="email">Email: </label>
                                        </div>
                                        <div class="col-md-12 col-lg-8">
                                            <input type="email" id="email" name="email" disabled/>
                                        </div>
                                    </div>
                                    <div class="row mx-4 my-3 border-bottom"></div>
                                    <div class="row my-2">
                                        <div class="col-md-12 col-lg-4 my-auto">
                                            <label for="nome">Nome: </label>
                                        </div>
                                        <div class="col-md-12 col-lg-8">
                                            <input type="text" id="nome" name="nome" required autofocus/>
                                        </div>
                                    </div>
                                    <div class="row my-2">
                                        <div class="col-md-12 col-lg-4 my-auto">
                                            <label for="cognome">Cognome: </label>
                                        </div>
                                        <div class="col-md-12 col-lg-8">
                                            <input type="text" id="cognome" name="cognome" required/>
                                        </div>
                                    </div>
                                </div>
                                <button type="button" class="btn btn-success my-2">Conferma</button>
                            </form>
                        </div>
                        <div id="security" class="section d-none">
                            <header class="my-2">
                                <h2>Sicurezza</h2>
                                <hr>
                            </header>
                            <form id="frmSecurity" class="mb-3">
                                <div class="form-group">
                                        <label for="nuova"><span>Vecchia password:</span></label>
                                        <input id="nuova" type="password" class="form-control my-1" name="old" placeholder="Inserisci la vecchia password" autocomplete="off" required />
                                        <label for="vecchia"><span>Nuova password:</span></label>
                                        <input id="vecchia" type="password" class="form-control my-2" name="new" placeholder="Nuova password" autocomplete="off" required />
                                        <label for="reinserisci"><span>Conferma nuova password:</span></label>
                                        <input id="reinserisci" type="password" class="form-control my-1" name="newk" placeholder="Conferma la nuova password" autocomplete="off" required />
                                </div>
                                <button class="btn btn-success my-2" type="button">Conferma</button>
                            </form>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </div>
</main>