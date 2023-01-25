<main class="big-margin">
    <div class="container-fluid p-0 overflow-hidden">
        <section>
            <header>
                <h1 class="text-center">Dati Avvistamento</h1>
            </header>
            <?php
                if(isset($_GET["id"])){
                    echo '
                    <input type="hidden" id="idcod" name="id" value="'.$_GET["id"].'"/>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-12 col-lg-6 lg-my-3 lg-px-3 px-4 py-0">
                                <div id="map"><div id="my-map"></div></div>
                            </div>
                            <div class="col-12 col-lg-6 lg-my-4 lg-px-3 px-4 py-0 my-0">
                                <div class="w-100 px-2 py-1 avvstaDiv">
                                    <h2>Specifiche</h2>
                                    <form action="" method="post">
                                        <div class="row my-2">
                                            <div class="col-6">
                                                <label for="utente">Utente:</label>
                                                <input id="utente" class="input-group" type="text" disabled name="utente"/>
                                            </div>
                                            <div class="col-6">
                                                <label for="data">Data: </label>
                                                <input id="data" class="input-group" type="datetime" min="2000-01-02" name="data"/>
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
                                                <label for="latitudine">Latitudine: </label>
                                                <input id="latitudine" class="input-group" type="number" step="0.000001" min="0" max="90" name="latitudine"/>
                                            </div>
                                            <div class="col-6">
                                                <label for="longitudine">Longitudine: </label>
                                                <input id="longitudine" class="input-group" type="number" step="0.000001" min="0" max="90" name="longitudine"/>
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
                                            <div class="col-12">
                                                <label for="img">Immagini: </label>
                                                <input id="img" class="input-group" type="file" name="file"/>
                                            </div>
                                        <div>
                                        <div class="row my-2">
                                            <div class="col-12">
                                                <div id="divimg"></div>
                                                <p>Mostrare le immagini già presenti<br> Solo per i delfini ci sarà la possibilità di cercare di riconoscere la specie, attraverso un bottone</p>
                                            </div>
                                        <div>
                                        <div class="row my-2">
                                            <div class="col-12 text-end">
                                                <button class="btn btn-primary btn-lg" type="button">Salva</button>
                                            </div>
                                        <div>
                                    </form>
                                </div>
        
                            </div>
                        </div>
                    </div>
                    ';
                }
            ?>
        </section>
    </div>
</main>