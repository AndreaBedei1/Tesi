<main class="big-margin">
    <div class="container-fluid p-0 overflow-hidden">
        <section>
            <header>
                <h1 class="text-center">Profilo Utente</h1>
            </header>
            <input id="user" type="hidden" value="<?php echo $_GET["id"]; ?>"/>
            <div class="row">
                <div class="col-md-1"></div>
                <div class="col-12 col-md-10">
                    <div class="card by-1">
                        <div class="text-white bg-dark">
                            <div class="row">
                                <div class="col-3 col-md-2">
                                    <img id="imgUtente" src="" alt="Immagine profilo utente" class="img-fluid img-thumbnail mx-2 my-2"/>
                                </div>
                                <div class="col-6 col-md-7 d-flex align-items-start flex-column">
                                    <p id="nominativo" class="my-2 h2">Nome e cognome</p>
                                    <p id="username" class="h5">Username</p>
                                </div>
                                <div class="col-3 col-md-3 d-flex flex-row-reverse">
                                    <button id="btnSegui" type="button" class="btn btn-outline-light mt-auto mb-auto mx-2" data-state="Segui">Segui</button>
                                </div>
                            </div>
                        </div>

                        <div class="card-body p-2 text-black">
                            <section>
                                <header>
                                    <h2>Informazioni</h2>
                                </header>
                                <div class="row">
                                    <div class="col-12 col-md-8">
                                        <div class="mb-2 p-1">
                                            <p id="sesso" class="font-italic mb-1"><strong>Sesso:</strong></p>
                                            <p id="nascita" class="font-italic mb-2"><strong>Nascita:</strong></p>
                                            <p id="interessi" class="font-italic mb-0"><strong>Interessi:</strong></p>
                                        </div>
                                    </div>
                                    <div class="col-12 col-md-4">
                                        <div class="d-flex justify-content-center justify-content-md-end text-center py-1 pe-md-5 user-stats">
                                            <div class="p-3 me-3 border stats-left">
                                                <p id="nSeguaci" class="mb-1 h5">---</p>
                                                <p class="small text-muted mb-0">Seguaci</p>
                                            </div>
                                            <div class="p-3 border stats-right">
                                                <p id="nSeguiti" class="mb-1 h5">---</p>
                                                <p class="small text-muted mb-0">Seguiti</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>
                            <section class="mb-2">
                                <header>
                                    <h2>Amicizie</h2>
                                </header>
                                <div class="container mw-100 mt-3 me-1 p-1">
                                    <div id="accordion">
                                        <div class="card">
                                            <div class="card-header p-0">
                                                <a id="btnSeguaci" class="collapsed btn w-100 p-3" data-bs-toggle="collapse" aria-expanded="false" href="#collapseOne">
                                                        <span class="list-item-title">Seguaci</span>
                                                        <img class="icon icon-primary" alt="Espandi seguaci" src="images/expand.svg"/>
                                                </a>
                                            </div>
                                            <div id="collapseOne" class="collapse" data-bs-parent="#accordion">
                                                <div class="card-body">
                                                    <ul id="seguaci" class="list-group list-group-light">
                                                        
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card">
                                            <div class="card-header p-0">
                                                <a id="btnSeguiti" class="collapsed btn w-100 p-3" data-bs-toggle="collapse" aria-expanded="false" href="#collapseTwo">
                                                    <span class="list-item-title">Seguiti</span>
                                                    <img class="icon icon-primary" alt="Espandi seguaci" src="images/expand.svg"/>
                                                </a>
                                            </div>
                                            <div id="collapseTwo" class="collapse" data-bs-parent="#accordion">
                                                <div class="card-body">
                                                    <ul id="seguiti" class="list-group list-group-light">
                                                        
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>
                            <section>
                                <h2>Post recenti</h2>
                                <div id="contPosts">
                                    <p>Nessun post da visualizzare.</p>
                                </div>
                            </section>
                            <div id="alertC" class="my-2" role="alert"></div>
                        </div>
                    </div>
                </div>
                <div class="col-md-1"></div>
            </div>
        </section>
    </div>
</main>