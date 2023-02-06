<main class="big-margin">
    <div id="dialog">
        <div id='add' class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="dialog" aria-hidden="true">
            <div class='modal-dialog modal-lg' role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2 class="modal-title">Aggiungi avvistamento</h2>
                        <button type="button" class="btn btn-outline-secondary btn-sm close" data-dismiss="modal" aria-label="chiusura">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="w-100 px-2 py-1">
                            <div id="alert"></div>
                            <form id="frmIns" action="#" method="post">
                                <div class="row my-2">
                                    <div class="col-6">
                                        <label for="data">Data*: </label>
                                        <input id="data" class="input-group" type="datetime-local" required min="2000-01-01T00:00" max="<?php echo date("Y-m-d");?>T23:59" name="data"/>
                                    </div>
                                    <div class="col-6">
                                        <label for="nEsemplari">Num. esemplari*: </label>
                                        <input id="nEsemplari" class="input-group" type="number" min="0" max="10" required name="esemplari"/>
                                    </div>
                                </div>
                                <div class="row my-2">
                                    <div class="col-6">
                                        <label for="latitudine">Latitudine*: </label>
                                        <input id="latitudine" class="input-group" type="number" step="0.000001" min="0" max="90" required name="latitudine"/>
                                    </div>
                                    <div class="col-6">
                                        <label for="longitudine">Longitudine*: </label>
                                        <input id="longitudine" class="input-group" type="number" step="0.000001" min="0" max="90" required name="longitudine"/>
                                    </div>
                                </div>
                                <div class="row my-2">
                                    <div class="col-6">
                                        <label for="slcSpecie">Animale: </label>
                                        <select name="specie" id="slcSpecie" class="form-select"></select>
                                    </div>
                                    <div class="col-6">
                                        <label for="slcSottospecie">Specie: </label>
                                        <select name="sottospecie" id="slcSottospecie" class="form-select" disabled></select>
                                    </div>
                                </div>
                                <div class="row my-2">
                                    <div class="col-6">
                                        <label for="mare">Mare (nodi): </label>
                                        <input id="mare" class="input-group" type="number" min="0" max="500" name="mare"/>
                                    </div>
                                    <div class="col-6">
                                        <label for="vento">Vento (km/h): </label>
                                        <input id="vento" class="input-group" type="number" min="0" max="500" name="vento"/>
                                    </div>
                                </div>
                                <div class="row my-2">
                                    <div class="col-12">
                                        <label for="note">Note: </label>
                                        <textarea name="note" id="note" class="input-group" ></textarea>
                                    </div>
                                </div>
                            </form>
                        </div>   
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Aggiungi</button>
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Annulla</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid p-0 overflow-hidden">
        <section>
            <header>
                <div class="p-1 bd-highlight text-center">
                    <h1>Tabella Avvistamenti</h1>
                </div>
                <div class="p-3 pt-0 bd-highlight text-end">
                    <button id="aggiungi" class="btn btn-success" aria-label="Aggiungi avvistamento"><span class="fas fa-plus"></span> Avvistamento</button>
                </div>
            </header>
            <div class="card-body">
                <div class="row">
                    <div class="col-12 col-lg-6 lg-my-3 lg-px-3 px-4 py-0">
                        <div id="map"><div id="my-map"></div></div>
                    </div>
                    <div class="col-12 col-lg-6 lg-my-4 lg-px-3 px-4 py-0 my-0">
                        <div id="divtbl" class="tableFixHead mb-3">
                            <table id="tblAvvistamenti" class="table">
                                <caption class="d-none">Tabella con elenco avvistamenti</caption>
                                <thead>
                                    <tr>
                                        <th id="utente" scope="col">Utente</th>
                                        <th id="data2" scope="col">Data</th>
                                        <th id="coordinate" scope="col">Zona</th>
                                        <th id="animale" scope="col">Animale</th>
                                        <th id="visualizza" scope="col">Vedi</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</main>