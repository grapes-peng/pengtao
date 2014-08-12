package com.encrypt;

/*
* ABchatRSA.java
*
* Created on 2008年6月28日, 下午12:38
*在苦苦寻找答案为什么会抛出 javax.crypto.BadPaddingException: Data must start with zero

*异常后我决定将正确的答案写出来供大家参考    

*大连理工大学 软件学院 郭晓鹤
* To change this template, choose Tools | Template Manager
* and open the template in the editor.
*/

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

/**
*
* @author GuoXiaoHe
*/
public class ABchatRSA {
    
    /** Creates a new instance of ABchatRSA */
    /*构建需要的函数变量*/
    private KeyPairGenerator kePaGen=null;                //秘密钥匙生成器;
    private KeyPair          keyPair=null;                //钥匙对，公尺 和米尺；
    private PublicKey        publicKey=null;              //共匙；
    private PrivateKey       privateKey=null;             //密匙；
    private int             keySize    =512;               //密匙长
    
    public ABchatRSA(int keysize) {
        this.keySize= keysize;
        try{
        this.kePaGen= KeyPairGenerator.getInstance("RSA"); //
        this.kePaGen.initialize(this.keySize);   
        //
        this.keyPair=this.kePaGen.genKeyPair();
        this.privateKey=this.keyPair.getPrivate();
        this.publicKey=this.keyPair.getPublic();
         //this.abcharRsaCipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");
        }catch( Exception err){
            err.printStackTrace();
        }
       
    }
    public PublicKey getPublicKey()
    {
        return this.publicKey;
    }
    public PrivateKey getPrivateKey()
    {
        return this.privateKey;
    }
    public static String encripyRSA(String platxt,PublicKey publickey)
    {
        String cipherStr=null;                              //返回的加密后的字符串;      
        byte[]plainByte=null;                              //获得明文的byte数组; 
        byte[]cipherByte;                                    //产生秘闻的byte数组;                          
        Cipher cipher =null;
        try{          
        plainByte=platxt.getBytes("ISO-8859-1"); 
        cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,publickey); 
        cipherByte=cipher.doFinal(plainByte);        
        cipherStr=new String(cipherByte,"ISO-8859-1"); 
         }catch(Exception err){
            err.printStackTrace();
            System.out.println("error in en: "+err.toString());
        }
        return cipherStr;
    }
    
    public static String decripyRSA(String cphtxt,PrivateKey privateKey)
    {
         byte[] cipherByte =null;                             //获得秘闻的byte数组;        
         byte[] plainByte   =null;                             //解密后的明文数组;
         String   plainStr    =null;                            //解密后的明文数组;
         Cipher   cipher      =null;                            //加密用;
        try{
            cipherByte       =cphtxt.getBytes("ISO-8859-1");    //统一使用该种编码方式;
            cipher =Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            plainByte=cipher.doFinal(cipherByte);
            plainStr=new String(plainByte,"ISO-8859-1");         
        }catch(Exception err)
        {

            err.printStackTrace();
        }
        return plainStr;
    }
    
    
    public static void main(String []args)
    {
        ABchatRSA arsa=new ABchatRSA(512);
        String en=ABchatRSA.encripyRSA("HELLO world",arsa.getPublicKey());      
        String de=ABchatRSA.decripyRSA(en,arsa.getPrivateKey());
        System.out.println(de);        
    }
}
