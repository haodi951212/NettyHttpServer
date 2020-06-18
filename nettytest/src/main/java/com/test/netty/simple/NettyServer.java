package com.test.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建两个线程组, 默认EventLoop个数是cpu核数*2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            //创建服务器端启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 120) //设置线程队列的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("客户socketChannel " + ch); //可以将它们加入一个集合，推送消息时，可以将业务加入到各个
                            // channel的NIOEventLoop的 taskQueue
                            ch.pipeline().addLast(new NettyServerHandler());
                        } //创建一个通道初始化对象c
                    });
            System.out.println("服务器...ready...");
            ChannelFuture cf = bootstrap.bind(6668).sync(); //绑定一个端口并同步，生成一个ChannelFuture对象,启动服务器
            cf.channel().closeFuture().sync();//对关闭通道进行监听
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
