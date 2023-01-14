<?php require("../template/intestazione.php")?>
<script src="./avvistamento.js"></script>
        <header>
            <h1 class="h1">Avvistamento</h1>
        </header>
        <main>
            <div class="card">
                <div class="card-body">
                    <div class="row">
                        <div class="col-sm-12 col-lg-6">
                            <div id="map"><div id ="my-map"></div></div>
                        </div>
                        <div class="col-sm-12 col-lg-6">
                            <h2>Specifiche</h2>
                            <form action="" method="post">
                                <div class="row">
                                    <div class="col-2">
                                        <label for="utente">Utente: </label>
                                    </div>
                                    <div class="col-3">
                                        <input id="utente" class="input-group" type="text" disabled name="utente">
                                    </div>
                                    <div class="col-2">
                                        <label for="data">Data: </label>
                                    </div>
                                    <div class="col-3">
                                        <input id="data" class="input-group" type="datetime" name="data">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-2">
                                        <label for="specie">Specie: </label>
                                    </div>
                                    <div class="col-3">
                                        <select name="specie" id="slcSpecie" class="form-select"></select>
                                    </div>
                                    <div class="col-2">
                                        <label for="sottospecie">Sottospecie: </label>
                                    </div>
                                    <div class="col-3">
                                        <select name="sottospecie" id="slcSottospecie" class="form-select"></select>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-2">
                                        <label for="latitudine">Latitudine: </label>
                                    </div>
                                    <div class="col-3">
                                        <input id="latitudine" class="input-group" type="number" step="0.000001" name="latitudine">
                                    </div>
                                    <div class="col-2">
                                        <label for="longitudine">Longitudine: </label>
                                    </div>
                                    <div class="col-3">
                                        <input id="longitudine" class="input-group" type="number" step="0.000001" name="longitudine">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-2">
                                        <label for="nEsemplari">Num. esemplari: </label>
                                    </div>
                                    <div class="col-3">
                                        <input id="nEsemplari" class="input-group" type="number" min="0" name="esemplari">
                                    </div>
                                    <div class="col-2">
                                        <label for="vento">Vento (km/h): </label>
                                    </div>
                                    <div class="col-3">
                                        <input id="vento" class="input-group" type="number" min="0" name="vento">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-2">
                                        <label for="mare">Mare (nodi): </label>
                                    </div>
                                    <div class="col-3">
                                        <input id="mare" class="input-group" type="number" min="0" name="mare">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-2">
                                        <label for="note">Note: </label>
                                    </div>
                                    <div class="col-8">
                                        <textarea name="note" id="note" class="input-group" ></textarea>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-2">
                                        <label for="img">Immagini: </label>
                                    </div>
                                    <div class="col-8">
                                        <input id="img" class="input-group" type="file" name="file">
                                    </div>
                                <div>
                                <div class="row">
                                    <div class="col-10">
                                        <div id="divimg"></div>
                                        <p>Mostrare le immagini già presenti<br> Solo per i delfini ci sarà la possibilità di cercare di riconoscere la specie, attraverso un bottone</p>
                                    </div>
                                <div>
                                <div class="row">
                                    <div class="col-6">
                                        <button class="btn btn-primary" type="button">Salva</button>
                                    </div>
                                <div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        
<?php require("../template/foot_pag.php")?>