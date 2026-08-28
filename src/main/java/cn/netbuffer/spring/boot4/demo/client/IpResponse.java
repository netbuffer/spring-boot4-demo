package cn.netbuffer.spring.boot4.demo.client;

public record IpResponse(String addr, String country, String province, String city, String isp) {
}
