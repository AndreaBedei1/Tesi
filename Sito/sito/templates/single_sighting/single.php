<?php
    if(!isset($_GET["id"])){
        header("location: homepage.php");
    }
?>
<main class="big-margin">
    <div class="container-fluid p-0 overflow-hidden">
        <section>
            <header>
                <h1 class="text-center">Dati Avvistamento</h1>
            </header>
            <div id='modal' class="modal fade modal-xl" tabindex="-1" role="dialog" aria-labelledby="Immagini" aria-hidden="true">
                <div class='modal-dialog modal-lg' role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2 class="modal-title" id="modaltitle">Immagini</h2>
                            <button type="button" class="btn btn-light btn-sm close" data-dismiss="modal" aria-label="Close">x</button>
                        </div>
                        <div class="modal-body">
                            <div id="alert" role="alert"></div>
                            <div class="my-2">
                                <h3>Immagini caricate</h3>
                                <div id="imgs">                
                                </div>
                            </div>
                            <form id="frmAddImg" action="" method="post">
                                <div class="form-group">
                                    <div class="">
                                        <label for="file">Carica Immagine:</label><br/>
                                        <input type="file" class="form-control" id="file"/>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button id="addImags" type="button" class="btn btn-primary">Carica</button>
                        </div>
                    </div>
                </div>
            </div>
            <div id='info' class="modal fade" tabindex="-1" role="dialog" aria-labelledby="dialog" aria-hidden="true">
                <div class='modal-dialog' role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2 class="modal-title"></h2>
                            <button type="button" class="btn btn-light btn-sm close" data-dismiss="modal" aria-label="Close">x</button>
                        </div>
                        <div class="modal-body">
                        </div>
                        <div class="modal-footer">
                        </div>
                    </div>
                </div>
            </div>
            <input type="hidden" id="idcod" name="id" value="<?php echo $_GET["id"] ?>"/>
            <div class="card-body w-100">
                <div class="row w-100">
                    <div class="col-12 col-lg-6 lg-my-2 lg-px-3 px-4 py-0 mt-lg-5">
                        <div id="map"><div id="my-map"></div></div>
                    </div>
                    <div class="col-12 col-lg-6 lg-mb-2 lg-px-3 px-4 py-0 my-0">
                        <div class="w-100 px-2 py-1 avvstaDiv">
                            <div class="row">
                                <div class="col-10">
                                    <h2>Specifiche</h2>
                                </div>
                                <div class="col-2 text-end">
                                    <button id="delete" class="btn btn-danger" type="button">Elimina</button>
                                </div>
                            </div>
                            <form id="avvDates" action="" method="post">
                                <div class="row my-2">
                                    <div class="col-6">
                                        <label for="utente">Utente:</label>
                                        <input id="utente" class="input-group" type="text" disabled name="utente"/>
                                    </div>
                                    <div class="col-6">
                                        <label for="data">Data: </label>
                                        <input id="data" class="input-group" type="datetime-local" disabled name="data"/>
                                    </div>
                                </div>
                                <div class="row my-2">
                                    <div class="col-6">
                                        <label for="latitudine">Latitudine: </label>
                                        <input id="latitudine" class="input-group" type="number" step="0.000001" min="0" max="90" disabled name="latitudine"/>
                                    </div>
                                    <div class="col-6">
                                        <label for="longitudine">Longitudine: </label>
                                        <input id="longitudine" class="input-group" type="number" step="0.000001" min="0" max="90" disabled name="longitudine"/>
                                    </div>
                                </div>
                                <div class="row my-2">
                                    <div class="col-6">
                                        <label for="specie">Specie: </label>
                                        <select name="specie" id="slcSpecie" class="form-select"></select>
                                    </div>
                                    <div class="col-6">
                                        <label for="sottospecie">Sottospecie: </label>
                                        <select name="sottospecie" id="slcSottospecie" class="form-select"></select>
                                    </div>
                                </div>
                                <div class="row my-2">
                                    <div class="col-6">
                                        <label for="nEsemplari">Num. esemplari: </label>
                                        <input id="nEsemplari" class="input-group" type="number" min="0" max="10" name="esemplari"/>
                                    </div>
                                    <div class="col-6">
                                        <label for="vento">Vento (km/h): </label>
                                        <input id="vento" class="input-group" type="number" min="0" max="500" name="vento"/>
                                    </div>
                                </div>
                                <div class="row my-2">
                                    <div class="col-6">
                                        <label for="mare">Mare (nodi): </label>
                                        <input id="mare" class="input-group" type="number" min="0" max="500" name="mare"/>
                                    </div>
                                </div>
                                <div class="row my-2">
                                    <div class="col-12">
                                        <label for="note">Note: </label>
                                        <textarea name="note" id="note" class="input-group" ></textarea>
                                    </div>
                                </div>
                                <div class="row my-2">
                                    <div class="col-6">
                                        <button id="btn_visual" class="btn btn-dark" type="button">Visualizza Immagini</button>
                                    </div>
                                    <div class="col-6 text-end">
                                        <button id="save" class="btn btn-primary" type="button">Salva</button>
                                    </div>
                                <div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</main>