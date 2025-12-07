<template>
    <div class="weui-tab">
        <div class="weui-tab__panel">
            <div class="weui-cells__title" @click="goBack">返回</div>
            <div class="hd">
                <h1 class="page_title">话题</h1>
            </div>
            <div id="data">
                <div v-for="topic in topicList" :key="topic.id" class="wb-item-wrap wb-item-wrap-{{topic.id}}">
                    <div class="wb-item wb-item-{{topic.id}}">
                        <div class="card m-panel card9 f-topic m-panel-{{topic.id}}">
                            <!-- 个人资料和发布时间 -->
                            <div class="card-wrap card-wrap-first-{{topic.id}}">
                                <header class="topic-top m-box m-box-{{topic.id}}">
                                    <div class="m-avatar-box m-box-center">
                                        <a :href="'/profile/user/' + topic.username" class="m-img-box">
                                            <img :id="'profile-avatar-' + topic.id" :src="topic.avatar || ''">
                                        </a>
                                    </div>
                                    <div class="m-box-dir m-box-col m-box-center m-box-dir-{{topic.id}}">
                                        <div class="m-text-box">
                                            <a :href="'/profile/user/' + topic.username">
                                                <h3 class="m-text-cut profile-nickname-{{topic.id}}">{{topic.nickname || topic.username}}</h3>
                                            </a>
                                            <h4 class="m-text-cut"><span class="time">{{topic.publishTime}}</span></h4>
                                        </div>
                                    </div>
                                </header>
                            </div>
                            <!-- 话题信息正文 -->
                            <article class="topic-main topic-main-{{topic.id}}">
                                <div class="card-wrap card-wrap-second-{{topic.id}}">
                                    <div class="topic-og topic-og-{{topic.id}}">
                                        <div class="topic-text">
                                            <a><span class="surl-text">#{{topic.topic}}# </span></a>{{topic.content}}
                                        </div>
                                        <div v-if="topic.count > 0" class="topic-image topic-image-{{topic.id}}">
                                            <div class="topic-media-wraps topic-media f-media media-b topic-media-{{topic.id}}">
                                                <ul class="m-auto-list m-auto-list-{{topic.id}}">
                                                    <li v-for="index in topic.count" :key="index" class="m-auto-box" :class="'m-auto-box' + (topic.count > 3 ? 3 : topic.count)">
                                                        <div class="m-img-box m-imghold-4-3">
                                                            <img :id="'f-bg-img-' + topic.id + '-' + index" :data-id="topic.id" :data-count="topic.count" :data-index="index" :src="topic.images[index-1] || ''" class="f-bg-img" @click="browseTopicImage(topic, index)">
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </article>
                            <footer class="f-footer-ctrl f-footer-ctrl-{{topic.id}}">
                                <aside>
                                    <div class="m-diy-btn m-box-center-a" @click="likeTopic(topic.id)">
                                        <i :id="'like-icon-' + topic.id" class="lite-iconf" :class="topic.liked ? 'lite-iconf-liked' : 'lite-iconf-like'"></i>
                                        <h4 :id="'like-count-' + topic.id">{{topic.likeCount}}</h4>
                                    </div>
                                </aside>
                            </footer>
                        </div>
                    </div>
                </div>
            </div>
            <p id="loadmore" v-if="topicList.length > 0" style="text-align: center; font-size: 14px; color: #777;margin-top: 15px;margin-bottom: 35px" @click="loadMoreTopicData" :disabled="loading || noMoreData">
                {{loading ? '加载中' : (noMoreData ? '已无更多信息' : '点击查看更多')}}<br>
                <i v-if="loading" class="weui-loading"></i>
            </p>
        </div>
        <!-- 加载中弹框 -->
        <div role="alert" id="loadingToast" v-if="loading" style="display: block;">
            <div class="weui-mask_transparent"></div>
            <div class="weui-toast">
                <span class="weui-primary-loading weui-icon_toast">
                  <span class="weui-primary-loading__dot"></span>
                </span>
                <p class="weui-toast__content">加载中</p>
            </div>
        </div>
        <div class="weui-tabbar">
            <a href="javascript:" @click="$router.push('/topic')" class="weui-tabbar__item weui-bar__item_on">
                <img src="/img/topic/home.png" class="weui-tabbar__icon">
                <p class="weui-tabbar__label">首页</p>
            </a>
            <a href="javascript:" @click="$router.push('/topic/publish')" class="weui-tabbar__item">
                <img src="/img/topic/publish.png" class="weui-tabbar__icon">
                <p class="weui-tabbar__label">发布</p>
            </a>
            <a href="javascript:" @click="$router.push('/topic/search')" class="weui-tabbar__item">
                <img src="/img/topic/search.png" class="weui-tabbar__icon">
                <p class="weui-tabbar__label">搜索</p>
            </a>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'TopicIndex',
    data() {
        return {
            start: 0,
            topicList: [],
            loading: false,
            noMoreData: false
        }
    },
    mounted() {
        // 默认加载最新的10条话题信息
        this.loadTopicData();
    },
    methods: {
        // 返回上一页
        goBack() {
            this.$router.back();
        },
        
        // 加载话题信息
        loadTopicData() {
            this.loading = true;
            axios.get(`/api/topic/start/${this.start}/size/10`)
                .then(response => {
                    this.loading = false;
                    if (response.data.success) {
                        if (response.data.data.length > 0) {
                            this.start += response.data.data.length;
                            // 为每个话题加载头像、昵称和图片
                            response.data.data.forEach(topic => {
                                this.loadProfile(topic);
                                this.loadTopicImages(topic);
                                this.topicList.push(topic);
                            });
                        } else {
                            this.noMoreData = true;
                        }
                    } else {
                        this.$toptip(response.data.message, 'error');
                    }
                })
                .catch(error => {
                    this.loading = false;
                    if (error.response) {
                        this.$toptip(error.response.data.message, 'error');
                    } else {
                        this.$toptip('网络连接失败,请检查网络连接', 'error');
                    }
                });
        },
        
        // 加载更多话题信息
        loadMoreTopicData() {
            if (!this.loading && !this.noMoreData) {
                this.loadTopicData();
            }
        },
        
        // 加载发布者头像和昵称信息
        loadProfile(topic) {
            topic.avatar = '';
            topic.nickname = '';
            
            // 加载头像
            axios.get(`/rest/avatar/${topic.username}`)
                .then(response => {
                    if (response.data.success && response.data.data) {
                        topic.avatar = response.data.data;
                    }
                })
                .catch(error => {
                    console.error('加载头像失败:', error);
                });
            
            // 加载昵称
            axios.get(`/rest/nickname/${topic.username}`)
                .then(response => {
                    if (response.data.success && response.data.data) {
                        topic.nickname = response.data.data;
                    }
                })
                .catch(error => {
                    console.error('加载昵称失败:', error);
                });
        },
        
        // 加载话题信息图片
        loadTopicImages(topic) {
            topic.images = [];
            for (let i = 1; i <= topic.count; i++) {
                axios.get(`/api/topic/id/${topic.id}/index/${i}/image`)
                    .then(response => {
                        if (response.data.success && response.data.data) {
                            if (!topic.images) topic.images = [];
                            topic.images[i-1] = response.data.data;
                        }
                    })
                    .catch(error => {
                        console.error('加载图片失败:', error);
                    });
            }
        },
        
        // 点赞话题信息
        likeTopic(id) {
            const topic = this.topicList.find(t => t.id === id);
            if (topic && !topic.liked) {
                this.loading = true;
                axios.post(`/api/topic/id/${id}/like`)
                    .then(response => {
                        this.loading = false;
                        if (response.data.success) {
                            topic.liked = true;
                            topic.likeCount++;
                        } else {
                            this.$toptip(response.data.message, 'error');
                        }
                    })
                    .catch(error => {
                        this.loading = false;
                        if (error.response) {
                            this.$toptip(error.response.data.message, 'error');
                        } else {
                            this.$toptip('网络连接失败,请检查网络连接', 'error');
                        }
                    });
            }
        },
        
        // 打开图片浏览器
        browseTopicImage(topic, index) {
            // 这里可以实现图片浏览器功能，暂时保持为空
            console.log('浏览图片:', topic.id, index);
        }
    }
}
</script>

<style scoped>
@import '../../../static/css/common/common.css';
@import '../../../static/css/common/weui.min.css';
@import '../../../static/css/topic/base.css';
@import '../../../static/css/topic/cards.css';
@import '../../../static/css/topic/app.css';
@import '../../../static/css/topic/profile.css';
</style>