/*
 * Copyright 2012 The Netty Project
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
//The MIT License
//
//Copyright (c) 2009 Carl Bystršm
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.

package org.wisdom.netty.client;

import com.google.protobuf.Parser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.CharsetUtil;

import java.net.URLDecoder;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("WebSocket Client disconnected!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                System.out.println("WebSocket Client connected!");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                System.out.println("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.getStatus() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            System.out.println("WebSocket Client received message: " + textFrame.text());
        } else if (frame instanceof PongWebSocketFrame) {
            System.out.println("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received closing");
            ch.close();
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame bwsf = (BinaryWebSocketFrame) frame;
            ByteBuf content = bwsf.content();
            int uuid = BytesUtil.byte2ToInt(new byte[]{content.readByte(), content.readByte()});
            int protoId = BytesUtil.byte2ToInt(new byte[]{content.readByte(), content.readByte()});

            System.out.println("uuid=" + uuid + ",protoId=" + protoId);
            // content.discardReadBytes();
            byte[] bytes = new byte[content.capacity() - content.readerIndex()];
            content.readBytes(bytes);
            byte[] data = new byte[bytes.length - 2];

            System.arraycopy(bytes, 2, data, 0, data.length);
            if (protoId == 2001) {
                Parser<CenterCmd.Info> parser = CenterCmd.Info.PARSER;
                CenterCmd.Info info = parser.parseFrom(data);
                System.out.println("errorCode:" + info.getErrorCode());
                System.out.println("description:" + info.getDescription());
            } else if (protoId == 3253) {
                Parser<CenterCmd.RecommendAnchorList> parser = CenterCmd.RecommendAnchorList.PARSER;
                CenterCmd.RecommendAnchorList anchorList = parser.parseFrom(data);
                for (CenterCmd.RecommendAnchorInfo anchorInfo : anchorList.getListList()) {
                    System.out.println("userId:" + anchorInfo.getUserId());
                    System.out.println("nickName:" + URLDecoder.decode(anchorInfo.getNickName(), "UTF-8"));
                    System.out.println("logoTime:" + anchorInfo.getLogoTime());
                    System.out.println("thirdIconurl:" + anchorInfo.getThirdIconurl());
                    System.out.println("sex:" + anchorInfo.getSex());
                    System.out.println("age:" + anchorInfo.getAge());
                }
            } else if (protoId == 2040) {
                Parser<CenterCmd.SearchData> parser = CenterCmd.SearchData.PARSER;
                CenterCmd.SearchData searchData = parser.parseFrom(data);
                // required int32 type = 1; //数据类型:1公会;2群组;3用户
                // required string id = 2;
                // required string name = 3;
                // required int32 logoTime = 4;
                // optional string gameId = 5; //绑定的游戏id(群组)
                // optional string unionId = 6; //公会ID(群组)
                // optional bool password = 7; //是否有密码
                // optional int32 onlineNum = 8; //在线数量
                // optional int32 jobId = 9; //职位ID
                // optional int32 unionType = 10; //公会类型  2:游戏公会 3:娱乐公会
                for (CenterCmd.SearchVo vo : searchData.getDatasList()) {
                    System.out.println("userId:" + vo.getId());
                    System.out.println("name:" + URLDecoder.decode(vo.getName(), "UTF-8"));
                    System.out.println("unionId:" + vo.getUnionId());
                    System.out.println("type:" + vo.getType());
                    System.out.println("hasPassward:" + vo.getPassword());
                    System.out.println("logoTime:" + vo.getLogoTime());
                    System.out.println("jobId:" + vo.getJobId());
                    System.out.println("onlineNum:" + vo.getOnlineNum());
                    System.out.println("unionType:" + vo.getUnionType());
                }
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }
}
