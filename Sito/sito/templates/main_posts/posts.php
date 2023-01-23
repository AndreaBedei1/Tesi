<main class="big-margin">
    <div class="container-fluid p-0 overflow-hidden">
        <div class="row">
            <div class="col-12 col-md-12">
                <section>
                    <header>
                        <h1 class="text-center">Bacheca</h1>
                    </header>
                    <a href="#" class="d-flex btn btn-1 btn-inverted rounded-circle p-2 image-wrapper-tiny" id="btnTop">
                        <img class="btn-image" src="images/chevron-up.svg" alt="Torna in cima alla pagina"/>
                    </a>
                    <div class="container">
                        <div class="row d-flex justify-content-center">
                            <div class="col-md-12 col-lg-10 col-xl-8">
                                <section>
                                    <header>
                                        <h2>Crea post</h2>
                                    </header>
                                    <div class="card">
                                        <div id="alert" class="my-2" role="alert"></div>
                                        <div class="card-body">
                                            <div class="d-flex flex-start align-items-center">
                                                <div class="image-wrapper-medium me-2">
                                                    <img id="userImage" class="rounded-circle shadow-1-strong" src="images/profile_img/profilo.jpg" alt="avatar user" width="60" height="60" />
                                                </div>
                                                <div>
                                                    <p class="fw-bold mb-1 text-left"><?php if(isset($_SESSION["user"])){echo $_SESSION["user"];} ?></p>
                                                </div>
                                            </div>
                                            <form id="formPost">
                                                <div class="mt-3 mb-1 pb-2">
                                                    <div class="form-outline w-100">
                                                        <label for="textAreaTitolo">Titolo: </label>
                                                        <textarea class="form-control" id="textAreaTitolo" rows="1" name="titolo" placeholder="Inserisci titolo" maxlength="100" required></textarea>
                                                    </div>
                                                </div>
                                                <div class="mb-1 pb-2">
                                                    <div class="form-outline w-100">
                                                        <label for="textAreaTesto">Testo: </label>
                                                        <textarea class="form-control" id="textAreaTesto" rows="4" name="testo" placeholder="Inserisci testo del post" maxlength="1000" required></textarea>
                                                    </div>
                                                </div>
                                                <div class="mb-1">
                                                    <label for="image" class="form-label">Seleziona immagine</label>
                                                    <input class="form-control form-control-sm" id="image" type="file" name="immagine" accept="image/*" />
                                                </div>
                                                <div class="mt-2 pt-1">
                                                    <label for="slcGenere">Tipologia post:</label>
                                                    <select class="form-select form-select-sm" name="tipo" id="slcGenere"></select>
                                                </div>
                                                <div class="float-end mt-2 pt-1">
                                                    <input id="btnAggiungi" type="button" class="btn btn-1 btn-sm" value="Aggiungi" />
                                                    <input type="reset" class="btn btn-2 btn-sm" value="Cancella" />
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </section>
                                <section id="sctId">
                                    <header>
                                        <h2>ULTIMI POST</h2>
                                        <div class="form-check form-switch">
                                            <input class="form-check-input" type="checkbox" id="allPost" />
                                            <label class="form-check-label" for="allPost">Visualizza solo i post dei seguiti</label>
                                        </div>
                                    </header>
                                    <div id="contPosts">
                                        <p>Nessun post da visualizzare.</p> 
                                    </div>
                                    <div id="alertC" class="my-2" role="alert"></div>
                                </section>
                            </div>
                        </div>
                    </div>
                </section>
                <div class="col-1 col-md-3"></div>
            </div>
        </div>
    </div>
</main>