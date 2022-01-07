# 问题

## 只能通过localhost连接，不能通过ip连接

C:\Program Files\PostgreSQL\13\data\pg_hda.conf

在# IPv4 local connections:下方加入：

```
host    all             all             0.0.0.0/0               trust
```

