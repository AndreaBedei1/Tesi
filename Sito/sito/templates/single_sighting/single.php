<?php
    if(!isset($_GET["id"])){
        header("location: homepage.php");
    }
?>
<main class="big-margin">
    <div id="dialog">
        <div id='info' class="modal fade" tabindex="-1" role="dialog" aria-labelledby="dialog" aria-hidden="true">
            <div class='modal-dialog' role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2 class="modal-title"></h2>
                        <button type="button" class="btn btn-outline-secondary btn-sm close" data-dismiss="modal" aria-label="Chiusura">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                    </div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid p-0 overflow-hidden">
        <section>
            <header>
                <h1 class="text-center">Dati Avvistamento</h1>
            </header>
            <input type="hidden" id="idcod" name="id" value="<?php echo $_GET["id"] ?>"/>
            <div class="card-body w-100">
                <div class="row w-100 p-0 m-0">
                    <div class="col-12 col-lg-6 lg-my-2 lg-px-3 px-2 py-0 mt-lg-5">
                        <div id="map"><div id="my-map"></div></div>
                    </div>
                    <div class="col-12 col-lg-6 lg-mb-2 lg-px-3 px-2 py-0 my-0 h-100">
                        <div class="w-100 px-2 py-1">
                            <div class="row">
                                <div class="col-6 p-0 px-2 m-0">
                                    <h2>Specifiche</h2>
                                </div>
                                <div class="col-6 text-end px-2">
                                    <button id="delete" class="btn btn-danger" type="button" aria-label="Elimina"><span class="fas fa-trash"></span> Avvistamento</button>
                                </div>
                            </div>
                            <form id="avvDates" class="m-0 p-0 w-100" action="#" method="post">
                                <div class="row form-group mb-1">
                                    <div class="col-6">
                                        <label class="form-label mb-1" for="utente">Utente:</label>
                                        <input id="utente" class="form-control" type="text" disabled name="utente"/>
                                    </div>
                                    <div class="col-6">
                                        <label class="form-label mb-1" for="data">Data: </label>
                                        <input id="data" class="form-control" type="datetime-local" disabled name="data"/>
                                    </div>
                                </div>
                                <div class="row form-group mb-1">
                                    <div class="col-6">
                                        <label class="form-label mb-1" for="latitudine">Latitudine: </label>
                                        <input id="latitudine" class="form-control" type="number" step="0.000001" min="0" max="90" name="latitudine"/>
                                    </div>
                                    <div class="col-6">
                                        <label class="form-label mb-1" for="longitudine">Longitudine: </label>
                                        <input id="longitudine" class="form-control" type="number" step="0.000001" min="0" max="90" name="longitudine"/>
                                    </div>
                                </div>
                                <div class="row form-group mb-1">
                                    <div class="col-6">
                                        <label class="form-label mb-1" for="slcSpecie">Animale: </label>
                                        <select name="specie" id="slcSpecie" class="form-select"></select>
                                    </div>
                                    <div class="col-6">
                                        <label class="form-label mb-1" for="slcSottospecie">Specie: </label>
                                        <div class=" w-100 d-flex">
                                            <select name="sottospecie" id="slcSottospecie" class="form-select"></select>
                                            <button id="infoSpecie" type="button" aria-label="Info Specie" class="btn btn-outline-dark rounded-circle m-1 btn-sm">
                                                <span class="fas fa-info"></span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <div class="row form-group mb-1">
                                    <div class="col-6">
                                        <label class="form-label mb-1" for="nEsemplari">Num. esemplari: </label>
                                        <input id="nEsemplari" class="form-control" type="number" min="0" max="10" name="esemplari"/>
                                    </div>
                                    <div class="col-6">
                                        <label class="form-label mb-1" for="vento">Vento (km/h): </label>
                                        <input id="vento" class="form-control" type="number" min="0" max="500" name="vento"/>
                                    </div>
                                </div>
                                <div class="row form-group mb-1">
                                    <div class="col-6">
                                        <label class="form-label mb-1" for="mare">Mare (nodi): </label>
                                        <input id="mare" class="form-control" type="number" min="0" max="500" name="mare"/>
                                    </div>
                                </div>
                                <div class="row form-group mb-1">
                                    <div class="col-12">
                                        <label class="form-label mb-1" for="note">Note: </label>
                                        <textarea name="note" id="note" rows="3" class="form-control"></textarea>
                                    </div>
                                </div>
                                <div class="row form-group mb-1">
                                    <div class="col-3">
                                        <button id="btn_visual" class="btn btn-success" type="button" aria-label="Aggiungi immagine"><span class="fas fa-plus"></span> Immagine</button>
                                    </div>
                                    <div class="col-6 text-center">
                                        <button id="btn_Rico" class="btn btn-info" type="button" aria-label="Individua specie"><span class="fas fa-search"></span> Riconoscimento</button>
                                        <button id="infoRic" type="button" aria-label="Info riconoscimento automatico" class="btn btn-outline-dark rounded-circle btn-sm">
                                                <span class="fas fa-info"></span>
                                        </button>
                                    </div>
                                    <div class="col-3 text-end">
                                        <button id="save" class="btn btn-primary" type="button"><span class="fas fa-save"></span> Salva</button>
                                    </div>
                                <div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="card animals mb-5 h-100">
                <div class="row mt-2">
                    <div class="col-12 p-2 h-100" id="imgs">                
                    </div>
                </div>
                <div id="divDel" class="row text-center rSotto">
                </div>
                <div class="row border border-secondary rounded mx-1 my-2 rSotto">
                    <div class="col-12 p-3 w-100 h-100" id="sighting">
                    </div>
                </div>
            </div>
        </section>
    </div>
</main>