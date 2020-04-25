/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.wisdom.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

public final class WebSocketClient {

    // static final String URL = System.getProperty("url", "ws://127.0.0.1:9092");
    static final String URL = System.getProperty("url", "ws://129.204.9.217:28042");
    // static final String URL = System.getProperty("url", "ws://test-wss-app.917youxi.com");

    public static void main(String[] args) throws Exception {
        URI uri = new URI(URL);
        String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        final int port;
        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }

        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            System.err.println("Only WS(S) is supported.");
            return;
        }

        final boolean ssl = "wss".equalsIgnoreCase(scheme);
        // final SslContext sslCtx;
        // if (ssl) {
        //     sslCtx = SslContextBuilder.forClient()
        //         .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        // } else {
        //     sslCtx = null;
        // }

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
            final WebSocketClientHandler handler =
                    new WebSocketClientHandler(
                            WebSocketClientHandshakerFactory.newHandshaker(
                                    uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));

            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            // if (sslCtx != null) {
                            //     p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                            // }
                            p.addLast(
                                    new HttpClientCodec(),
                                    new HttpObjectAggregator(8192),
                                    handler);
                        }
                    });

            Channel ch = b.connect(uri.getHost(), port).sync().channel();
            handler.handshakeFuture().sync();

            // BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            // while (true) {
            //     String msg = console.readLine();
            //     if (msg == null) {
            //         break;
            //     } else if ("bye".equals(msg.toLowerCase())) {
            //         ch.writeAndFlush(new CloseWebSocketFrame());
            //         ch.closeFuture().sync();
            //         break;
            //     } else if ("ping".equals(msg.toLowerCase())) {
            //         WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
            //         ch.writeAndFlush(frame);
            //     } else {
            //         WebSocketFrame frame = new TextWebSocketFrame(msg);
            //         ch.writeAndFlush(frame);
            //     }
            // }
            BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));

            ch.writeAndFlush(binaryWebSocketFrame);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            Integer uuid = 0;
            while (true) {
                String msg = console.readLine();
                byte[] ubs = BytesUtil.intToBytes2(uuid++);
                System.out.println("uuid=" + BytesUtil.byte2ToInt(ubs));
                if (StringUtils.isBlank(msg)) {
                    msg = "1232";
                }
                int protoId = Integer.parseInt(msg);
                List<Byte> content = new LinkedList<>();
                byte[] pb = BytesUtil.intToBytes2(protoId);
                System.out.println("protoId=" + BytesUtil.byte2ToInt(pb));
                byte[] pkg = new byte[ubs.length + pb.length];
                System.arraycopy(ubs, 0, pkg, 0, ubs.length);
                System.arraycopy(pb, 0, pkg, ubs.length, pb.length);

                if (protoId == 1232) {
                    byte[] bytes = CenterCmd.searchUser.newBuilder().setUserId("100001").setStart(1).setEnd(5).setKeyword("100007").build().toByteArray();
                    byte[] temp = new byte[pkg.length + bytes.length];
                    System.arraycopy(pkg, 0, temp, 0, pkg.length);
                    System.arraycopy(bytes, 0, temp, pkg.length, bytes.length);
                    pkg = temp;
                }
                binaryWebSocketFrame = new BinaryWebSocketFrame(Unpooled.wrappedBuffer(pkg));
                ch.writeAndFlush(binaryWebSocketFrame);
            }

        } finally {
            group.shutdownGracefully();
        }
    }

}
