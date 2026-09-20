public class CentroComercial {

    private Fila fila;
    private Tiempo tiempo;
    private Caja[] cajas;

    private int[] longitudFila;

    private final int NUMERO_CAJAS = 4;

    private final double PROBABILIDAD_LLEGADA = 0.60;
    private final double PROBABILIDAD_APERTURA_CAJA = 0.40;

    private final double PROBABILIDAD_PREFERENTE = 0.20;
    private final double PROBABILIDAD_COLARSE = 0.15;
    private final double PROBABILIDAD_ENTREGAR_COMPRAS = 0.10;

    public CentroComercial() {

        fila = new Fila();
        tiempo = new Tiempo();

        cajas = new Caja[NUMERO_CAJAS];

        for (int i = 0; i < cajas.length; i++) {
            cajas[i] = new Caja();
        }

        longitudFila = new int[120];
    }

    public void ejecutar() {

        while (!tiempo.haFinalizado()) {

            tiempo.avanzar();

            procesarLlegada();

            procesarAccionesEspeciales();

            abrirCaja();

            atenderClientes();

            registrarLongitudFila();

            mostrarEstado();
        }

        mostrarResumen();
        mostrarLongitudFila();
    }

    private void procesarLlegada() {

        if (Math.random() < PROBABILIDAD_LLEGADA) {

            Cliente cliente = crearCliente();

            if (cliente.tieneAtencionPreferente()) {
                fila.añadirPreferente(cliente);
            } else {
                fila.añadirCliente(cliente);
            }
        }
    }

    private Cliente crearCliente() {

        boolean preferente = Math.random() < PROBABILIDAD_PREFERENTE;

        return new Cliente(tiempo.obtenerMinuto(), preferente);
    }

    private void procesarAccionesEspeciales() {

        if (tiempo.obtenerMinuto() < 20) {
            return;
        }

        if (tiempo.obtenerMinuto() % 5 == 0) {
            fila.comprobarAburrimiento(tiempo.obtenerMinuto());
        }

        if (Math.random() < PROBABILIDAD_COLARSE) {

            if (!fila.estaLlena()) {

                Cliente cliente = new Cliente(
                        tiempo.obtenerMinuto(),
                        false
                );

                fila.colocarDetrasDeConocido(cliente);
            }
        }

        if (Math.random() < PROBABILIDAD_ENTREGAR_COMPRAS) {
            fila.entregarCompras();
        }

        if (tiempo.obtenerMinuto() % 15 == 0) {

            if (fila.obtenerNumeroClientes() > 25) {
                System.out.println(
                        "AVISO: pasen por esta caja en orden de fila"
                );
            }
        }
    }

    private void abrirCaja() {

        if (Math.random() < PROBABILIDAD_APERTURA_CAJA) {

            for (int i = 0; i < cajas.length; i++) {

                if (!cajas[i].estaAbierta()) {
                    cajas[i].abrir();
                    break;
                }
            }
        }
    }

    private void atenderClientes() {

        for (int i = 0; i < cajas.length; i++) {
            cajas[i].atender();
        }

        for (int i = 0; i < cajas.length; i++) {

            if (cajas[i].puedeAtender() && fila.hayGente()) {

                Cliente cliente = fila.sacarPrimero();

                cajas[i].añadirCliente(cliente);
            }
        }
    }

    private void registrarLongitudFila() {

        int minuto = tiempo.obtenerMinuto();

        longitudFila[minuto - 1] = fila.obtenerNumeroClientes();
    }

    private void mostrarEstado() {

        System.out.println();
        System.out.println("----------------------------------");
        System.out.println("MINUTO: " + tiempo.obtenerMinuto());
        System.out.println("----------------------------------");

        fila.mostrar();

        System.out.println();

        for (int i = 0; i < cajas.length; i++) {

            System.out.print("Caja " + (i + 1) + ": ");

            if (!cajas[i].estaAbierta()) {
                System.out.println("cerrada");
            } else if (cajas[i].estaLibre()) {
                System.out.println("libre");
            } else {
                System.out.println("atendiendo");
            }
        }
    }

    private void mostrarResumen() {

        int clientesAtendidos = 0;

        for (int i = 0; i < cajas.length; i++) {
            clientesAtendidos += cajas[i].obtenerClientesAtendidos();
        }

        System.out.println();
        System.out.println("==================================");
        System.out.println("RESUMEN FINAL");
        System.out.println("==================================");

        System.out.println(
                "Personas atendidas: " + clientesAtendidos
        );

        System.out.println(
                "Personas que quedan en fila: "
                        + fila.obtenerNumeroClientes()
        );
    }

    private void mostrarLongitudFila() {

        System.out.println();
        System.out.println("==================================");
        System.out.println("LONGITUD DE LA FILA");
        System.out.println("==================================");

        for (int i = 0; i < longitudFila.length; i++) {

            System.out.println(
                    "Minuto " + (i + 1)
                            + ": "
                            + longitudFila[i]
                            + " metros"
            );
        }
    }
}