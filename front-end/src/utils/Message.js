const message = {
    'Message0001': "检索结果零件，请重新设定检索条件。",
    'Message0002': "{$1}是必须的。",
    'Message0003': "{$1}不存在，请重新输入。",
    'Message0004': "非Admin用户无法导入过去年份的数据，请联系Admin。",
    'Message0005': "当前用户无法导入此套数据，请联系Admin。",
    'Message0006': "用户不存在!",
    'Message0007': "认证失败!",
    'Message0008': "密码重置成功,请重新登录!",
    'Message0009': "组织信息不存在!",
    'Message0010': "最大可查询年份为当前年的前一年。",
};

export function getMsg(messageID) {
    if (messageID.includes('Message')) {
        return message[messageID];
    } else {
        return messageID;
    }

}