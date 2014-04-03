package uk.co.fuuzetsu.bathroute.Engine;

import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class CommunicationManager {

    private final String HOST = "10.0.2.2";
    private final int PORT = 7777;
    private final Socket s;
    private final DataOutputStream sout;
    private final DataInputStream sin;
    private final BufferedReader reader;

    public CommunicationManager(final String host, final int port)
        throws IOException, UnknownHostException {
        this.s = new Socket(host, port);
        this.sout = new DataOutputStream(s.getOutputStream());
        this.sin =  new DataInputStream(s.getInputStream());
        this.reader = new BufferedReader(new InputStreamReader(this.sin));
    }

    public void write(byte[] b) throws IOException {
        this.sout.write(b);
        this.sout.flush();
    }

    public void write(String s) throws IOException {
        write(s.getBytes(Charset.forName("UTF-8")));
    }

    public void write(JSONObject js) throws IOException {
        write(js.toString());
    }

    public JSONObject read() throws JSONException, IOException {
        return new JSONObject(reader.readLine());
    }

    /* JSONObject for ServerMessage, private key of the device, public
     * key of the server. This effectively creates an encrypted
     * JSON-ified ServerComm.
     */
    public void sendMessage(RSAPrivateKey pk, RSAPublicKey pu,
                            RSAPublicKey sk,
                            JSONObject o) throws IOException {
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, sk);

            /* Encrypt message itself */
            byte[] bo = o.toString().getBytes(Charset.forName("UTF-8"));
            byte[] msg = c.doFinal(bo);

            /* Sign it */
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initSign(pk);
            sig.update(msg);
            byte[] signature = sig.sign();

            /* Encode the message and signature to Base64 so we can
               stick it into JSON */
            String sig64 = Base64.encodeToString(signature, Base64.DEFAULT);
            String msg64 = Base64.encodeToString(msg, Base64.DEFAULT);
            String key64 = Base64.encodeToString(pu.getEncoded(), Base64.DEFAULT);

            /* Create the JSON structure */
            JSONObject js = new JSONObject();
            js.put("senderKey", key64);
            js.put("senderSignature", sig64);
            js.put("serverMessage", msg64);

            /* Lastly encrypt the structure itself and send it down
             * the wire. */
            byte[] oo = js.toString().getBytes(Charset.forName("UTF-8"));
            write(c.doFinal(oo));

        } catch (IllegalBlockSizeException e) {
            Log.e("CommunicationManager",
                  "IllegalBlockSizeException:\n"
                  + ExceptionUtils.getStackTrace(e));
        } catch (InvalidKeyException e) {
            Log.e("CommunicationManager",
                  "InvalidKeyException:\n"
                  + ExceptionUtils.getStackTrace(e));
        }  catch (NoSuchAlgorithmException e) {
            Log.e("CommunicationManager",
                  "NoSuchAlgorithmException:\n"
                  + ExceptionUtils.getStackTrace(e));
        }  catch (BadPaddingException e) {
            Log.e("CommunicationManager",
                  "BadPaddingException:\n"
                  + ExceptionUtils.getStackTrace(e));
        }  catch (NoSuchPaddingException e) {
            Log.e("CommunicationManager",
                  "NoSuchPaddingException:\n"
                  + ExceptionUtils.getStackTrace(e));
        }  catch (SignatureException e) {
            Log.e("CommunicationManager",
                  "SignatureException:\n"
                  + ExceptionUtils.getStackTrace(e));
        }  catch (JSONException e) {
            Log.e("CommunicationManager",
                  "JSONException:\n"
                  + ExceptionUtils.getStackTrace(e));
        }
    }
}
