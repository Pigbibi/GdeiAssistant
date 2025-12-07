<template>
    <div class="weui-tab">
        <div class="weui-tab__panel">
            <div class="weui-cells__title" @click="goBack">返回</div>
            
            <div class="hd">
                <h1 class="page_title">校园公众号</h1>
                <p class="page_desc">热门校园资讯信息、优质社团组织推送，全在这里了</p>
            </div>
            
            <div>
                <!-- 所有热门公众号列表 -->
                <div v-for="(wechataccount, index) in wechatAccountList" :key="index" class="weui-form-preview" style="margin-bottom: 10px">
                    <div class="weui-form-preview__bd">
                        <div class="weui-form-preview__item" style="margin-bottom: 0.3rem">
                            <label class="weui-form-preview__label">头像</label>
                            <img :src="wechataccount.avatar" style="width: 50px; height: auto">
                        </div>
                        <div class="weui-form-preview__item">
                            <label class="weui-form-preview__label">公众号</label>
                            <span class="weui-form-preview__value">{{ wechataccount.name }}</span>
                        </div>
                        <div class="weui-form-preview__item">
                            <label class="weui-form-preview__label">功能介绍</label>
                            <span class="weui-form-preview__value">{{ wechataccount.description }}</span>
                        </div>
                        <div v-if="wechataccount.article" class="weui-form-preview__item">
                            <label class="weui-form-preview__label">最近文章</label>
                            <span class="weui-form-preview__value">{{ wechataccount.article }}</span>
                        </div>
                    </div>
                    <div class="weui-form-preview__ft">
                        <a class="weui-form-preview__btn weui-form-preview__btn_primary"
                           :href="`https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=${wechataccount.biz}&scene=123#wechat_redirect`">查看</a>
                    </div>
                </div>
            </div>
            
            <p class="page_desc">微信公众号由微信公众平台提供服务<br>公众号未被收录？点击此处
                <a class="page_desc" href="javascript:" @click="submitWechatAccount">进行提交</a>
            </p>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'WechatAccount',
    data() {
        return {
            wechatAccountList: []
        };
    },
    mounted() {
        this.loadWechatAccounts();
    },
    methods: {
        // 返回上一页
        goBack() {
            this.$router.back();
        },
        
        // 加载校园公众号列表
        loadWechatAccounts() {
            axios.get('/api/info/wechataccounts')
                .then(response => {
                    this.wechatAccountList = response.data;
                })
                .catch(error => {
                    console.error('加载校园公众号列表失败:', error);
                    this.$toptip('加载失败，请重试', 'error');
                });
        },
        
        // 提交校园公众号
        submitWechatAccount() {
            window.location.href = 'mailto:admin@gdeiassistant.cn?subject=提交校园公众号&body=请补全此模板邮件中的空缺信息，并将邮件发送到admin@gdeiassistant.cn。公众号至少发送过一篇文章推送，否则不予以收录。微信公众号由微信公众平台提供服务，本站仅进行信息收录、展示和推送。运营者在微信公众号运营和推广过程中需遵守微信公众平台和广东二师助手平台的相关规则。%0d%0a%0d%0a公众号名称：%0d%0a公众号微信号：%0d%0a公众号主体：（个人填写账号管理员姓名，非个人的其他如个体户、政府、媒体、企业以及其他组织等主体类型填写机构组织的名称）%0d%0a公众号类型：（填写订阅号/服务号，不支持企业微信）%0d%0a公众号说明：（填写公众号的功能和介绍）';
        }
    }
};
</script>

<style scoped>
@import '../../../static/css/common/common.css';
@import '../../../static/css/common/weui.min.css';
</style>