
<?php require("../template/intestazione.php")?>
        <script src="./tabella_dati.js"></script>
        <header>
            <h1 class="h1">Tabella degli avvistamenti</h1>
        </header>

        <main>
            <div class="card">
                <div class="card-body">
                    <div class="row">
                        <div class="col-6">
                            <div id="map"><div id="my-map"></div></div>
                        </div>
                        <div class="col-6">
                            <div id="divtbl" class="table-wrapper-scroll-y">
                                <table id="tblAvvistamenti" class="table table-bordered">
                                    <thead>
                                      <tr>
                                            <th id="utente" scope="col">Utente</th>
                                            <th id="data" scope="col">Data</th>
                                            <th id="latitudine" scope="col">Latitudine</th>
                                            <th id="longitudine" scope="col">Longitudine</th>
                                            <th id="soggetto" scope="col">Soggetto</th>
                                            <th id="visualizza" scope="col">Operazioni</th>
                                      </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        
<?php require("../template/foot_pag.php")?>