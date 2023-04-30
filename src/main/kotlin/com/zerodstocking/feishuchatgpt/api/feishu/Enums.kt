package com.zerodstocking.feishuchatgpt.api.feishu

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2022/12/5 14:40
 */

enum class TokenType(val value: Int) {
    TENANT(1),
    APP(2)
}

enum class BotActivateStatus(val value: Int, val description: String) {

    INIT(0, "初始化，租户待安装"),

    DEACTIVATE(1, "租户停用"),

    ENABLE(2, "租户启用"),

    AFTER_INSTALLATION_TO_ENABLED(3, "安装后待启用"),

    UPGRADE_TO_ENABLED(4, "升级待启用"),

    LICENSE_EXPIRE_DEACTIVATION(5, "license过期停用"),

    PACKAGE_EXPIRATION(6, "Lark套餐到期或降级停用")
}