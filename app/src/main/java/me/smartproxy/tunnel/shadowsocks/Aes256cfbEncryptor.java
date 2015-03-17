package me.smartproxy.tunnel.shadowsocks;

import java.nio.ByteBuffer;
import java.util.Random;

import me.smartproxy.crypto.CryptoUtils;
import me.smartproxy.tunnel.IEncryptor;

public class Aes256cfbEncryptor implements IEncryptor {

    private static final int KEY_SIZE = 32;

    private static final int IV_SIZE = 16;

    private byte[] key, iv_send, iv_recv;

    private static final String TAG = "Aes256cfbEncryptor";

    private long id;

    public Aes256cfbEncryptor(String password) {
        id = new Random(System.currentTimeMillis()).nextLong();
        CryptoUtils.initEncryptor(password, "aes-256-cfb", id);
    }

    @Override
    public ByteBuffer encrypt(ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            int bufferSize = buffer.remaining();
            byte[] encryptionBuff = new byte[bufferSize];
            buffer.get(encryptionBuff, buffer.position(), buffer.remaining());
            //Log.e(TAG, "buff len is " + encryptionBuff.length);
            //Log.e(TAG, "buffer want to send\n " + new String(encryptionBuff));
            byte[] cipher = CryptoUtils.encrypt(encryptionBuff, id);
            return ByteBuffer.wrap(cipher);
        }
        return buffer;
    }

    @Override
    public ByteBuffer decrypt(ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            byte[] decryptBuff = new byte[buffer.remaining()];
            buffer.get(decryptBuff, buffer.position(), buffer.remaining());
            byte[] result = CryptoUtils.decrypt(decryptBuff, id);
            return ByteBuffer.wrap(result);
        }
        return buffer;
    }
}
