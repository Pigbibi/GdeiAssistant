<template>
    <div class="page" id="spare-room">
        <!-- 返回按钮 -->
        <div class="back-btn" @click="goBack">
            <i class="iconfont icon-fanhui"></i>
        </div>
        
        <!-- 标题 -->
        <div class="title">
            <h1>空课室查询</h1>
        </div>
        
        <!-- 查询条件表单 -->
        <div id="form" class="weui-cells weui-cells_form">
            <div class="weui-cell">
                <div class="weui-cell__hd">
                    <label for="zone" class="weui-label">校区</label>
                </div>
                <div class="weui-cell__bd">
                    <select id="zone" v-model="formData.zone" class="weui-select weui-input">
                        <option value="0">不限</option>
                        <option value="1">海珠</option>
                        <option value="2">花都</option>
                        <option value="3">广东轻工南海校区</option>
                        <option value="4">业余函授校区</option>
                    </select>
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd">
                    <label for="type" class="weui-label">教室类别</label>
                </div>
                <div class="weui-cell__bd">
                    <select id="type" v-model="formData.type" class="weui-select weui-input">
                        <option value="0">不限</option>
                        <option value="1">不用课室的课程</option>
                        <option value="2">操场</option>
                        <option value="3">大多媒体</option>
                        <option value="4">电脑专业机房</option>
                        <option value="5">雕塑室</option>
                        <option value="6">多媒体教室</option>
                        <option value="7">翻译室</option>
                        <option value="8">服装实验室</option>
                        <option value="9">钢琴室</option>
                        <option value="10">钢琴室</option>
                        <option value="11">公共机房</option>
                        <option value="12">国画临摹室</option>
                        <option value="13">画室</option>
                        <option value="14">化学实验室</option>
                        <option value="15">机房</option>
                        <option value="16">教具室</option>
                        <option value="17">教育实验室</option>
                        <option value="18">解剖实验室</option>
                        <option value="19">金融数学实验室</option>
                        <option value="20">美术课室</option>
                        <option value="21">蒙氏教学法专用课室</option>
                        <option value="22">模型制作实验室</option>
                        <option value="23">平面制作实验室</option>
                        <option value="24">琴房</option>
                        <option value="25">摄影实验室</option>
                        <option value="26">声乐课室</option>
                        <option value="27">生物实验室</option>
                        <option value="28">实训室</option>
                        <option value="29">视唱练耳室</option>
                        <option value="30">陶艺室</option>
                        <option value="31">体操房</option>
                        <option value="32">网络实验室</option>
                        <option value="33">微格课室</option>
                        <option value="34">无须课室</option>
                        <option value="35">舞蹈室</option>
                        <option value="36">舞蹈室</option>
                        <option value="37">物理实验室</option>
                        <option value="38">小多媒体</option>
                        <option value="39">小多媒体(&lt;70)</option>
                        <option value="40">小普通课室(&lt;70)</option>
                        <option value="41">小组课室</option>
                        <option value="42">形体房</option>
                        <option value="43">音乐室</option>
                        <option value="44">音乐专业课室</option>
                        <option value="45">语音室</option>
                        <option value="46">智能录像室</option>
                        <option value="47">中多媒体(70-100)</option>
                        <option value="48">专业课教室</option>
                        <option value="49">专业理论课室</option>
                        <option value="50">专业实验室</option>
                        <option value="51">咨询室</option>
                        <option value="52">综合绘画实验室</option>
                    </select>
                </div>
            </div>
            <!-- 座位数大于等于 -->
            <div class="weui-cell">
                <div class="weui-cell__hd"><label for="minSeating" class="weui-label">座位数</label></div>
                <div class="weui-cell__bd">
                    <input id="minSeating" v-model="formData.minSeating" class="weui-input" type="number" pattern="[0-9]*" placeholder="大于等于，选填"/>
                </div>
            </div>

            <!-- 座位数小于等于 -->
            <div class="weui-cell">
                <div class="weui-cell__hd"><label for="maxSeating" class="weui-label">座位数</label></div>
                <div class="weui-cell__bd">
                    <input id="maxSeating" v-model="formData.maxSeating" class="weui-input" type="number" pattern="[0-9]*" placeholder="小于等于，选填"/>
                </div>
            </div>

            <!-- 起始周数 -->
            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label class="weui-label">起始周数</label>
                </div>
                <div class="weui-cell__bd">
                    <select id="startTime" v-model="formData.startTime" class="weui-select">
                        <option v-for="i in 20" :key="i" :value="i">{{i}}周</option>
                    </select>
                </div>
            </div>

            <!-- 结束周数 -->
            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label class="weui-label">结束周数</label>
                </div>
                <div class="weui-cell__bd">
                    <select id="endTime" v-model="formData.endTime" class="weui-select">
                        <option v-for="i in 20" :key="i" :value="i">{{i}}周</option>
                    </select>
                </div>
            </div>

            <!-- 最小星期数 -->
            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label class="weui-label">从星期</label>
                </div>
                <div class="weui-cell__bd">
                    <select id="minWeek" v-model="formData.minWeek" class="weui-select">
                        <option value="0">一</option>
                        <option value="1">二</option>
                        <option value="2">三</option>
                        <option value="3">四</option>
                        <option value="4">五</option>
                        <option value="5">六</option>
                        <option value="6">日</option>
                    </select>
                </div>
            </div>

            <!-- 最大星期数 -->
            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label class="weui-label">到星期</label>
                </div>
                <div class="weui-cell__bd">
                    <select id="maxWeek" v-model="formData.maxWeek" class="weui-select">
                        <option value="0">一</option>
                        <option value="1">二</option>
                        <option value="2">三</option>
                        <option value="3">四</option>
                        <option value="4">五</option>
                        <option value="5">六</option>
                        <option value="6">日</option>
                    </select>
                </div>
            </div>

            <!-- 单双周 -->
            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label class="weui-label">单双周</label>
                </div>
                <div class="weui-cell__bd">
                    <select id="weekType" v-model="formData.weekType" class="weui-select">
                        <option value="0">不限</option>
                        <option value="1">单</option>
                        <option value="2">双</option>
                    </select>
                </div>
            </div>

            <!-- 节数 -->
            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label class="weui-label">节数</label>
                </div>
                <div class="weui-cell__bd">
                    <select id="classNumber" v-model="formData.classNumber" class="weui-select">
                        <option value="0">第1,2节</option>
                        <option value="1">第3节</option>
                        <option value="2">第4,5节</option>
                        <option value="3">第6,7节</option>
                        <option value="4">第8,9节</option>
                        <option value="5">第10,11,12节</option>
                        <option value="6">上午</option>
                        <option value="7">下午</option>
                        <option value="8">晚上</option>
                        <option value="9">白天</option>
                        <option value="10">整天</option>
                    </select>
                </div>
            </div>
        </div>
        
        <!-- 查询按钮 -->
        <div class="weui-btn-area">
            <a class="weui-btn weui-btn_primary" href="javascript:" @click="postQueryForm">查询</a>
        </div>
        
        <!-- 查询结果区域 -->
        <div id="result" v-if="showResult">
            <div class="weui-cells__title">空课室列表</div>
            <div class="weui-cells">
                <div v-for="(room, index) in spareRoomList" :key="index" class="weui-cell weui-cell_access open-popup" @click="showDetail(index)">
                    <div class="weui-cell__bd">
                        <p>{{room.zone}}-{{room.type}}</p>
                    </div>
                    <div class="weui-cell__ft">{{room.name}}</div>
                </div>
            </div>
        </div>
        
        <!-- 详情弹窗 -->
        <div id="detail" class="weui-popup__container" v-show="showDetailPopup">
            <div class="weui-popup__overlay"></div>
            <div class="weui-popup__modal">
                <div class="toolbar">
                    <div class="toolbar-inner">
                        <i class="iconfont icon-fanhui" @click="closeDetail"></i>
                        <h1 class="title">课室详情</h1>
                    </div>
                </div>
                <div class="modal-content">
                    <div class="weui-cells weui-cells_form" v-if="selectedRoom">
                        <div class="weui-cell">
                            <div class="weui-cell__hd">
                                <label class="weui-label">课室名称</label>
                            </div>
                            <div class="weui-cell__bd">
                                <input type="text" class="weui-input" :value="selectedRoom.name" readonly>
                            </div>
                        </div>
                        <div class="weui-cell">
                            <div class="weui-cell__hd">
                                <label class="weui-label">课室类型</label>
                            </div>
                            <div class="weui-cell__bd">
                                <input type="text" class="weui-input" :value="selectedRoom.type" readonly>
                            </div>
                        </div>
                        <div class="weui-cell">
                            <div class="weui-cell__hd">
                                <label class="weui-label">校区</label>
                            </div>
                            <div class="weui-cell__bd">
                                <input type="text" class="weui-input" :value="selectedRoom.campus" readonly>
                            </div>
                        </div>
                        <div class="weui-cell">
                            <div class="weui-cell__hd">
                                <label class="weui-label">楼层</label>
                            </div>
                            <div class="weui-cell__bd">
                                <input type="text" class="weui-input" :value="selectedRoom.floor" readonly>
                            </div>
                        </div>
                        <div class="weui-cell">
                            <div class="weui-cell__hd">
                                <label class="weui-label">课桌椅数</label>
                            </div>
                            <div class="weui-cell__bd">
                                <input type="text" class="weui-input" :value="selectedRoom.classSeating" readonly>
                            </div>
                        </div>
                        <div class="weui-cell">
                            <div class="weui-cell__hd">
                                <label class="weui-label">考试桌椅数</label>
                            </div>
                            <div class="weui-cell__bd">
                                <input type="text" class="weui-input" :value="selectedRoom.examSeating" readonly>
                            </div>
                        </div>
                        <div class="weui-cell">
                            <div class="weui-cell__hd">
                                <label class="weui-label">区域</label>
                            </div>
                            <div class="weui-cell__bd">
                                <input type="text" class="weui-input" :value="selectedRoom.zone" readonly>
                            </div>
                        </div>
                        <div class="weui-cell">
                            <div class="weui-cell__hd">
                                <label class="weui-label">状态</label>
                            </div>
                            <div class="weui-cell__bd">
                                <input type="text" class="weui-input" value="空闲" readonly>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'SpareRoom',
    data() {
        return {
            formData: {
                zone: '0',
                type: '0',
                minSeating: '',
                maxSeating: '',
                startTime: '1',
                endTime: '20',
                minWeek: '0',
                maxWeek: '1',
                weekType: '0',
                classNumber: '0'
            },
            spareRoomList: [],
            showResult: false,
            showDetailPopup: false,
            selectedRoom: null
        }
    },
    mounted() {
        // 初始化默认周数
        this.loadWeeks();
    },
    methods: {
        // 返回上一页
        goBack() {
            this.$router.back();
        },
        
        // 加载可用周数
        loadWeeks() {
            axios.get('/api/spareroom/weeks')
                .then(response => {
                    if (response.data.success && response.data.data && response.data.data.length > 0) {
                        this.formData.minWeek = response.data.data[0].toString();
                        this.formData.maxWeek = response.data.data[response.data.data.length - 1].toString();
                    }
                })
                .catch(error => {
                    console.error('加载周数失败:', error);
                });
        },
        
        // 提交查询表单
        postQueryForm() {
                // 验证表单
            if (parseInt(this.formData.startTime) > parseInt(this.formData.endTime)) {
                this.$toptip('起始时间不能晚于结束时间', 'error');
                return;
            }
            if (parseInt(this.formData.minWeek) >= parseInt(this.formData.maxWeek)) {
                this.$toptip('起始星期数不能晚于或等于结束星期数', 'error');
                return;
            }
            
            // 显示加载提示
            this.$showLoading('查询中');
            
            // 发送查询请求
            axios.post('/api/spareroom/query', this.formData)
                .then(response => {
                    this.$hideLoading();
                    if (response.data.success) {
                        this.spareRoomList = response.data.data;
                        this.showResult = true;
                    } else {
                        this.$toptip(response.data.message, 'error');
                        this.showResult = false;
                    }
                })
                .catch(error => {
                    this.$hideLoading();
                    this.$toptip('查询失败，请稍后重试', 'error');
                    console.error('查询空课室失败:', error);
                });
        },
        
        // 显示课室详情
        showDetail(index) {
            const room = this.spareRoomList[index];
            this.selectedRoom = {
                ...room,
                classSeating: room.classSeating || '',
                examSeating: room.examSeating || ''
            };
            this.showDetailPopup = true;
        },
        
        // 关闭详情弹窗
        closeDetail() {
            this.showDetailPopup = false;
        }
    }
}
</script>

<style scoped>
@import '../../../../../static/css/common/common.css';
@import '../../../../../static/css/weui.min.css';

.back-btn {
    position: absolute;
    left: 15px;
    top: 15px;
    z-index: 10;
    color: #fff;
    font-size: 24px;
}

.title {
    text-align: center;
    padding: 15px 0;
    background-color: #1aad19;
    color: #fff;
}

.title h1 {
    font-size: 18px;
    font-weight: normal;
    margin: 0;
}

#form {
    margin-top: 0;
}

.weui-cells__title {
    margin-top: 20px;
}

.toolbar {
    position: relative;
    height: 44px;
    text-align: center;
    line-height: 44px;
    background-color: #1aad19;
    color: #fff;
}

.toolbar .icon-fanhui {
    position: absolute;
    left: 15px;
    top: 0;
    font-size: 24px;
}

.toolbar .title {
    background: transparent;
    font-size: 16px;
}

.modal-content {
    padding: 15px;
}
</style>