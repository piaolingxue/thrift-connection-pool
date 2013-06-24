namespace java com.huaban.thrift

service CalcService {
    void ping(),
    i32 add(1:i32 a, 2:i32 b)
}
