package domain.state;

/**
 * Interfaz base para el patrón State que gobierna el ciclo de vida de los Créditos Halcón en Escrow.
 */
public interface EscrowState {

    /**
     * Deposita créditos en la cuenta de custodia.
     */
    void deposit(EscrowContext context, double amountCredits);

    /**
     * Bloquea los créditos bajo custodia una vez confirmado el inicio del intercambio.
     */
    void lockInHold(EscrowContext context);

    /**
     * Libera los créditos a favor del estudiante prestador del servicio tras confirmación.
     */
    void releaseCredits(EscrowContext context, String studentConfirmationSecret);

    /**
     * Inicia un proceso de disputa cuando surge un desacuerdo en el intercambio.
     */
    void dispute(EscrowContext context, String reason);

    /**
     * Reembolsa los créditos al estudiante emisor tras resolución o cancelación válida.
     */
    void refund(EscrowContext context, String authorizationToken);

    /**
     * Retorna el identificador del estado actual.
     */
    String getStateName();
}
