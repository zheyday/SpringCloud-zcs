--判断用户是否存在success列表中
if redis.call('hexists',KEYS[2],KEYS[3]) ==1 then
--    用户已存在
    return -2;
end
local number = tonumber(redis.call('get',KEYS[1]))
if number <= 0 then
--    数量不足
    return -1;
end
return redis.call('DECRBY',KEYS[1],1);