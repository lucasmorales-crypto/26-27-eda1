public class Fila {

    private Cliente[] clientes;
    private int numeroClientes;

    private final int MAXIMO_PERSONAS = 30;
    private final double PROBABILIDAD_ABURRIRSE = 0.30;

    public Fila() {
        clientes = new Cliente[MAXIMO_PERSONAS];
        numeroClientes = 0;
    }

    public boolean estaLlena() {
        return numeroClientes >= MAXIMO_PERSONAS;
    }

    public boolean hayGente() {
        return numeroClientes > 0;
    }

    public int obtenerNumeroClientes() {
        return numeroClientes;
    }

    public Cliente obtenerPrimero() {
        if (hayGente()) {
            return clientes[0];
        }

        return null;
    }

    public boolean añadirCliente(Cliente cliente) {

        if (estaLlena()) {
            return false;
        }

        clientes[numeroClientes] = cliente;
        numeroClientes++;

        return true;
    }

    public Cliente sacarPrimero() {

        if (!hayGente()) {
            return null;
        }

        Cliente cliente = clientes[0];

        for (int i = 1; i < numeroClientes; i++) {
            clientes[i - 1] = clientes[i];
        }

        numeroClientes--;

        return cliente;
    }

    public boolean añadirPreferente(Cliente cliente) {

        if (estaLlena()) {
            return false;
        }

        int posicion = 0;

        for (int i = 0; i < numeroClientes; i++) {

            if (clientes[i].tieneAtencionPreferente()) {
                posicion = i + 1;
            }
        }

        for (int i = numeroClientes; i > posicion; i--) {
            clientes[i] = clientes[i - 1];
        }

        clientes[posicion] = cliente;
        numeroClientes++;

        return true;
    }

    public boolean colocarDetrasDeConocido(Cliente cliente) {

        if (estaLlena()) {
            return false;
        }

        if (!hayGente()) {
            return añadirCliente(cliente);
        }

        int posicion = (int) (Math.random() * numeroClientes);

        for (int i = numeroClientes; i > posicion + 1; i--) {
            clientes[i] = clientes[i - 1];
        }

        clientes[posicion + 1] = cliente;
        numeroClientes++;

        return true;
    }

    public boolean entregarCompras() {

        if (numeroClientes < 2) {
            return false;
        }

        int personaQueEntrega = (int) (Math.random() * numeroClientes);
        int personaQueRecibe = (int) (Math.random() * numeroClientes);

        while (personaQueRecibe == personaQueEntrega) {
            personaQueRecibe = (int) (Math.random() * numeroClientes);
        }

        if (clientes[personaQueEntrega].tieneCompras()) {
            clientes[personaQueEntrega].entregarCompras();
            return true;
        }

        return false;
    }

    public void comprobarAburrimiento(int minutoActual) {

        for (int i = 0; i < numeroClientes; i++) {

            if (clientes[i].minutosEnFila(minutoActual) > 8) {

                if (Math.random() < PROBABILIDAD_ABURRIRSE) {
                    eliminarCliente(i);
                    i--;
                }
            }
        }
    }

    private void eliminarCliente(int posicion) {

        for (int i = posicion + 1; i < numeroClientes; i++) {
            clientes[i - 1] = clientes[i];
        }

        numeroClientes--;
    }

    public void mostrar() {

        System.out.println("FILA: " + numeroClientes + " personas");

        for (int i = 0; i < numeroClientes; i++) {
            if (clientes[i].tieneAtencionPreferente()) {
                System.out.println("  Persona " + (i + 1) + " (preferente)");
            } else {
                System.out.println("  Persona " + (i + 1));
            }
        }
    }
}