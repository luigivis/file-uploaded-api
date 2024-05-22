package com.shuttleconsulting.fileuploadedapi.utils;

import com.shuttleconsulting.fileuploadedapi.dto.encrypt.EncryptDto;
import com.shuttleconsulting.fileuploadedapi.exception.DecryptException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Service
public class Encryption {

  private static final String CLAVE = "SECRET_PASSWORD**";
  private static final SecretKeySpec SECRET_KEY = createKey();

  private static SecretKeySpec createKey() {

    try {
      var encryptionSecretCode = CLAVE.getBytes(StandardCharsets.UTF_8);
      var sha = MessageDigest.getInstance("SHA-256");
      encryptionSecretCode = sha.digest(encryptionSecretCode);

      encryptionSecretCode = Arrays.copyOf(encryptionSecretCode, 16);

      return new SecretKeySpec(encryptionSecretCode, "AES");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Secret Key Gen Failed");
    }
  }

  public String encrypt(Object data) {
    var secretKey = createKey();
    var encryptString = "";
    try {
      var cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);

      var encryptData = data.toString().getBytes(StandardCharsets.UTF_8);
      var encryptBytes = cipher.doFinal(encryptData);
      encryptString = Base64.getEncoder().encodeToString(encryptBytes);
    } catch (NoSuchPaddingException
        | IllegalBlockSizeException
        | NoSuchAlgorithmException
        | BadPaddingException
        | InvalidKeyException e) {
      throw new RuntimeException("Encrypt Failed");
    }

    return encryptString;
  }

  public String decrypt(String encryptData) {
    var datos = "";

    try {
      var cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);

      var encryptByte = Base64.getDecoder().decode(encryptData);
      var decryptData = cipher.doFinal(encryptByte);
      datos = new String(decryptData);

    } catch (NoSuchAlgorithmException
        | InvalidKeyException
        | BadPaddingException
        | IllegalBlockSizeException
        | NoSuchPaddingException e) {
      throw new DecryptException("Token invalid.");
    }
    return datos;
  }

  public static void main(String[] args) {
    var enc = new Encryption();

    log.info("Current day {}", LocalDate.now().toEpochSecond(LocalTime.now(), ZoneOffset.UTC));
    var decrypt =
        enc.decrypt(
            "pOAt1iglcsT77Wd785JdYJQWXPdy8o3lFUdDr2fXAgFmaPy5SwN/+P4xPrSDf4BHmmh8EbZGmIM3RkcsoSr/l/yjslradx4PPZaQc3xxgExj5YGuPNoWfvugj0SIi+RMogXLfpckrPgnPXdXv/KirKTUsWBO8yFP1ZLk0Mby2Kb+D6i8UzHFRzo54HTD3w2SCCkb5gq7tOtqkhlqeNE0YfARAfr0Qi+FDCJzqetE8qK9r7cJuYJmtjagtJa9ljTP");
    log.info("Decrypt {}", decrypt);
    log.info("Username {}", new EncryptDto().toEncryptDtoFromJson(decrypt).getUsername());
  }
}
