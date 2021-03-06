package ftp_server.utils;

import java.net.InetSocketAddress;

public class FtpConfiguration {
    private InetSocketAddress inetSocketAddress;
    private String outputPath = "File.pdf";
    private int BufferSize = 2048;

    public FtpConfiguration(){
        inetSocketAddress = new InetSocketAddress("127.0.0.1",1111);
    }
    public void setConfiguration(String outputPath, int BufferSize){
        this.outputPath = outputPath;
        this.BufferSize = BufferSize;
    }

    public void setAddress(String host,int port){
        inetSocketAddress = new InetSocketAddress(host,port);
    }

    public InetSocketAddress getInetAddress(){
        return inetSocketAddress;
    }

    public String getOutputPath(){
        return outputPath;
    }

    public int getBufferSize(){
        return BufferSize;
    }
}
