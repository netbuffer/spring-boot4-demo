package cn.netbuffer.spring.boot4.demo.client;

public record IpQueryResult(int code, String message, IpResponse data) {
}
