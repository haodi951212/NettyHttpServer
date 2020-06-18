package com.test.netty.http;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;

@ChannelHandler.Sharable
public class TestServerInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //Netty提供的http的编解码器
        pipeline.addLast("myHttpServerCodec", new HttpServerCodec());
        pipeline.addLast("myHttpServerHandler", new TestHttpServerHandler());
    }
}
