<template>
  <div>
    <div>
      <div class="weui-cells__title" style="float: left" @click="goBack">返回</div>
      <div class="weui-cells__title" style="float: right" @click="showOptionMenu">更多</div>
      <div class="hd">
        <h1 class="page_title" style="clear:both;margin-top: 35px">我的课程表</h1>
      </div>
    </div>

    <!-- 选择查询的周数 -->
    <div @click="selectQueryWeek" style="width:100%;height:auto;box-sizing: border-box;margin-bottom:10px;">
      <div style="height:3rem;width:100%;line-height: 3rem;text-align: center;position:relative">
        <p id="currentWeek">{{ currentWeek }}</p>
        <span style="margin-left:35px;position: absolute;top: 0;display: inline-block;width:1rem;height:100%">
          <img style="width:0.7rem;margin-top: 1.18rem;" src="/img/schedule/select.png">
        </span>
      </div>
    </div>

    <table>
      <tr>
        <th></th>
        <th>周一</th>
        <th>周二</th>
        <th>周三</th>
        <th>周四</th>
        <th>周五</th>
        <th>周六</th>
        <th>周日</th>
      </tr>
      <tr v-for="row in 10" :key="row">
        <td>{{ row }}</td>
        <td v-for="col in 7" :key="col" :class="{ 'hidden': isCellHidden(row, col) }" 
            :rowspan="getCellRowspan(row, col)" 
            :style="getCellStyle(row, col)" 
            :data-index="getCellIndex(row, col)"
            @click="showScheduleDetail(row, col)">
          {{ getCellContent(row, col) }}
        </td>
      </tr>
    </table>

    <!-- 添加自定义课程窗口 -->
    <div id="addCustomScheduleDialog" class="weui-popup__container">
      <div class="weui-popup__overlay"></div>
      <div class="weui-popup__modal">
        <div class="toolbar">
          <div class="toolbar-inner">
            <a href="javascript:" style="left:0" class="picker-button close-popup" @click="isDialogOpen = false">取消</a>
            <h1 class="title">添加自定义课程</h1>
          </div>
        </div>
        <div class="modal-content">
          <div id="error" style="display: none;color: red;" class="weui-cells__title">{{ errorMessage }}</div>
          <div class="weui-cells">
            <div class="weui-cell">
              <div class="weui-cell__hd">
                <label class="weui-label">课程名称</label>
              </div>
              <div class="weui-cell__bd">
                <input id="scheduleName" v-model="customSchedule.scheduleName" class="weui-input" type="text" maxlength="50"
                       placeholder="请填写课程名称，不超过50字" @keyup="inputLengthCheck(customSchedule.scheduleName, 50)">
              </div>
            </div>
            <div class="weui-cell">
              <div class="weui-cell__hd">
                <label class="weui-label">上课地点</label>
              </div>
              <div class="weui-cell__bd">
                <input id="scheduleLocation" v-model="customSchedule.scheduleLocation" class="weui-input" type="text" maxlength="25"
                       placeholder="请填写上课地点，不超过25字" @keyup="inputLengthCheck(customSchedule.scheduleLocation, 25)">
              </div>
            </div>
            <div class="weui-cell">
              <div class="weui-cell__hd">
                <label class="weui-label">上课时间</label>
              </div>
              <div class="weui-cell__bd">
                <input type="hidden" id="row" v-model="customSchedule.row">
                <input type="hidden" id="column" v-model="customSchedule.column">
                <a id="timeSelector" href="javascript:" @click="selectScheduleTime">{{ customSchedule.timeDisplay }}</a>
              </div>
            </div>
            <div class="weui-cell">
              <div class="weui-cell__hd">
                <label class="weui-label">上课时长</label>
              </div>
              <div class="weui-cell__bd">
                <input type="hidden" id="length" v-model="customSchedule.scheduleLength">
                <a id="lengthSelector" href="javascript:" @click="selectScheduleLength">{{ customSchedule.lengthDisplay }}</a>
              </div>
            </div>
            <div class="weui-cell">
              <div class="weui-cell__hd">
                <label class="weui-label">上课周数</label>
              </div>
              <div class="weui-cell__bd">
                <input type="hidden" id="min_week" v-model="customSchedule.minScheduleWeek">
                <input type="hidden" id="max_week" v-model="customSchedule.maxScheduleWeek">
                <a id="weekSelector" href="javascript:" @click="selectScheduleWeek">{{ customSchedule.weekDisplay }}</a>
              </div>
            </div>
          </div>
          <!-- 提交按钮 -->
          <div class="weui-btn_area">
            <a id="submit" class="weui-btn weui-btn_primary" href="javascript:" @click="addCustomSchedule">提交</a>
          </div>
        </div>
      </div>
    </div>

    <!-- 加载中提示框 -->
    <div role="alert" id="loadingToast" style="display: none;">
      <div class="weui-mask_transparent"></div>
      <div class="weui-toast">
            <span class="weui-primary-loading weui-icon_toast">
              <span class="weui-primary-loading__dot"></span>
            </span>
        <p class="weui-toast__content">加载中</p>
      </div>
    </div>

    <!-- 查询成功弹框 -->
    <div id="toast" style="display:none">
      <div class="weui-mask_transparent"></div>
      <div class="weui-toast">
        <i class="weui-icon-success-no-circle weui-icon_toast"></i>
        <p class="weui-toast__content">加载课表成功</p>
      </div>
    </div>

    <!-- 普通课程详细信息窗口 -->
    <div id="scheduleDetailDialog" class="weui-popup__container">
      <div class="weui-popup__overlay"></div>
      <div class="weui-popup__modal">
        <div class="toolbar">
          <div class="toolbar-inner">
            <a href="javascript:" style="left:0" class="picker-button" @click="closeScheduleDetailDialog">关闭</a>
            <h1 class="title">课程详细信息</h1>
          </div>
        </div>
        <div class="modal-content">
          <div class='weui-form-preview' v-if="currentSchedule">
            <div class='weui-form-preview__hd'>
              <label class='weui-form-preview__label' 
                     style='max-width: 12rem;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;'>
                {{ currentSchedule.scheduleName }}
              </label>
              <em class='weui-form-preview__value'>{{ currentSchedule.scheduleType || '自定义课程' }}</em>
            </div>
            <div class='weui-form-preview__bd'>
              <div class='weui-form-preview__item'>
                <label class='weui-form-preview__label'>课程长度</label>
                <span class='weui-form-preview__value'>{{ currentSchedule.scheduleLength }}节</span>
              </div>
              <div class='weui-form-preview__item'>
                <label class='weui-form-preview__label'>上课节数</label>
                <span class='weui-form-preview__value'>{{ currentSchedule.scheduleLesson }}</span>
              </div>
              <div class='weui-form-preview__item' v-if="currentSchedule.scheduleTeacher">
                <label class='weui-form-preview__label'>任课教师</label>
                <span class='weui-form-preview__value'>{{ currentSchedule.scheduleTeacher }}</span>
              </div>
              <div class='weui-form-preview__item'>
                <label class='weui-form-preview__label'>上课地点</label>
                <span class='weui-form-preview__value'>{{ currentSchedule.scheduleLocation }}</span>
              </div>
              <div class='weui-form-preview__item'>
                <label class='weui-form-preview__label'>上课周数</label>
                <span class='weui-form-preview__value'>第{{ currentSchedule.minScheduleWeek }}周至第{{ currentSchedule.maxScheduleWeek }}周</span>
              </div>
            </div>
            <div style="display: none" class='weui-form-preview__ft'>
              <button type='submit' class='weui-form-preview__btn weui-form-preview__btn_primary'
                      href='javascript:'>删除自定义课程
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 自定义课程详细信息窗口 -->
    <div id="customScheduleDetailDialog" class="weui-popup__container">
      <div class="weui-popup__overlay"></div>
      <div class="weui-popup__modal">
        <div class="toolbar">
          <div class="toolbar-inner">
            <a href="javascript:" style="left:0" class="picker-button" @click="closeCustomScheduleDetailDialog">关闭</a>
            <h1 class="title">课程详细信息</h1>
          </div>
        </div>
        <div class="modal-content">
          <div v-for="(schedule, index) in currentMergedSchedule.detailScheduleList" 
               :key="index"
               class='weui-form-preview' style="margin-bottom: 10px;">
            <input type="hidden" name="id" :value="schedule.id">
            <input type="hidden" name="index" :value="index">
            <div class='weui-form-preview__hd'>
              <label class='weui-form-preview__label' 
                     style='max-width: 10rem;overflow: hidden;text-overflow:ellipsis;white-space: nowrap;'>
                {{ schedule.scheduleName }}
              </label>
              <em class='weui-form-preview__value'>自定义课程</em>
            </div>
            <div class='weui-form-preview__bd'>
              <div class='weui-form-preview__item'>
                <label class='weui-form-preview__label'>课程长度</label>
                <span class='weui-form-preview__value'>{{ schedule.scheduleLength }}节</span>
              </div>
              <div class='weui-form-preview__item'>
                <label class='weui-form-preview__label'>上课节数</label>
                <span class='weui-form-preview__value'>{{ schedule.scheduleLesson }}</span>
              </div>
              <div class='weui-form-preview__item'>
                <label class='weui-form-preview__label'>上课地点</label>
                <span class='weui-form-preview__value'>{{ schedule.scheduleLocation }}</span>
              </div>
              <div class='weui-form-preview__item'>
                <label class='weui-form-preview__label'>上课周数</label>
                <span class='weui-form-preview__value'>第{{ schedule.minScheduleWeek }}周至第{{ schedule.maxScheduleWeek }}周</span>
              </div>
            </div>
            <div class='weui-form-preview__ft'>
              <button type='submit' class='weui-form-preview__btn weui-form-preview__btn_primary'
                      @click="deleteCustomSchedule(schedule.id, currentMergedSchedule.index, index)">
                删除自定义课程
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Schedule',
  data() {
    return {
      isDialogOpen: false,
      hasDeleteAnyCustomSchedule: false,
      dataList: [],
      currentWeek: '选择周数',
      errorMessage: '',
      currentSchedule: null,
      currentMergedSchedule: null,
      customSchedule: {
        scheduleName: '',
        scheduleLocation: '',
        row: '',
        column: '',
        scheduleLength: '',
        minScheduleWeek: '',
        maxScheduleWeek: '',
        timeDisplay: '点击选择上课时间',
        lengthDisplay: '点击选择上课时长',
        weekDisplay: '点击选择上课周数'
      },
      columnPicker: [
        { label: '周一', value: 0 },
        { label: '周二', value: 1 },
        { label: '周三', value: 2 },
        { label: '周四', value: 3 },
        { label: '周五', value: 4 },
        { label: '周六', value: 5 },
        { label: '周日', value: 6 }
      ],
      rowPicker: [
        { label: '第一节', value: 0 },
        { label: '第二节', value: 1 },
        { label: '第三节', value: 2 },
        { label: '第四节', value: 3 },
        { label: '第五节', value: 4 },
        { label: '第六节', value: 5 },
        { label: '第七节', value: 6 },
        { label: '第八节', value: 7 },
        { label: '第九节', value: 8 },
        { label: '第十节', value: 9 }
      ],
      lengthPicker: [
        { label: '单独一节', value: 1 },
        { label: '连续两节', value: 2 },
        { label: '连续三节', value: 3 },
        { label: '连续四节', value: 4 },
        { label: '连续五节', value: 5 }
      ],
      weekPicker: Array.from({ length: 20 }, (_, i) => ({ label: `第${i + 1}周`, value: i + 1 }))
    };
  },
  mounted() {
    this.postQueryForm();
    this.setTextColor();
    
    // 监听返回事件
    if (window.history && window.history.pushState) {
      window.history.pushState('forward', null, './schedule');
      window.addEventListener('popstate', this.handlePopState);
    }
  },
  beforeDestroy() {
    window.removeEventListener('popstate', this.handlePopState);
  },
  methods: {
    goBack() {
      if (this.isDialogOpen) {
        this.isDialogOpen = false;
        window.history.pushState('forward', null, './schedule');
        $.closePopup();
      } else {
        sessionStorage.removeItem("scheduleWeek");
        this.$router.go(-1);
      }
    },
    handlePopState() {
      if (this.isDialogOpen) {
        this.isDialogOpen = false;
        window.history.pushState('forward', null, './schedule');
        $.closePopup();
      } else {
        sessionStorage.removeItem("scheduleWeek");
        this.$router.go(-1);
      }
    },
    inputLengthCheck(str, maxLen) {
      let w = 0;
      for (let i = 0; i < str.length; i++) {
        let c = str.charCodeAt(i);
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
          w++;
        } else {
          w += 2;
        }
        if (w > maxLen) {
          this.customSchedule.scheduleName = str.substr(0, i);
          break;
        }
      }
    },
    selectQueryWeek() {
      weui.picker(this.weekPicker, {
        defaultValue: [parseInt(sessionStorage.getItem("scheduleWeek") || 1)],
        onConfirm: (result) => {
          sessionStorage.setItem("scheduleWeek", result[0].value);
          this.postQueryForm();
        }
      });
    },
    showAddCustomScheduleDialog() {
      this.isDialogOpen = true;
      $("#addCustomScheduleDialog").popup();
    },
    selectScheduleTime() {
      weui.picker(this.columnPicker, this.rowPicker, {
        defaultValue: [0, 0],
        onConfirm: (result) => {
          this.customSchedule.column = result[0].value;
          this.customSchedule.row = result[1].value;
          this.customSchedule.timeDisplay = result[0].label + result[1].label;
          this.customSchedule.scheduleLength = "";
          this.customSchedule.lengthDisplay = "点击选择上课时长";
        }
      });
    },
    selectScheduleLength() {
      if (this.customSchedule.row === '') {
        this.errorMessage = "请先选择上课时间";
        $("#error").show().delay(2000).hide(0);
      } else {
        let maxLength = 0;
        if (this.customSchedule.row <= 4) {
          maxLength = 4 - this.customSchedule.row;
        } else if (this.customSchedule.row <= 9) {
          maxLength = 9 - this.customSchedule.row;
        }
        
        let availableLengthPicker = this.lengthPicker.map(item => ({
          ...item,
          disabled: item.value > maxLength + 1
        }));
        
        weui.picker(availableLengthPicker, {
          defaultValue: [1],
          onConfirm: (result) => {
            this.customSchedule.scheduleLength = result[0].value;
            this.customSchedule.lengthDisplay = result[0].label;
          }
        });
      }
    },
    selectScheduleWeek() {
      let startWeekPicker = this.weekPicker;
      let endWeekPicker = this.weekPicker;
      
      weui.picker(startWeekPicker, endWeekPicker, {
        defaultValue: [1, 1],
        onChange: (result) => {
          // 这里可以添加逻辑来禁用无效的结束周数选项
        },
        onConfirm: (result) => {
          if (result[0].value > result[1].value) {
            this.errorMessage = "起始周数不能大于结束周数";
            $("#error").show().delay(2000).hide(0);
          } else {
            this.customSchedule.minScheduleWeek = result[0].value;
            this.customSchedule.maxScheduleWeek = result[1].value;
            this.customSchedule.weekDisplay = result[0].label + result[1].label;
          }
        }
      });
    },
    deleteCustomSchedule(id, dataListIndex, detailListIndex) {
      $.showLoading("删除中");
      $("button[type='submit']").attr("disabled", true);
      
      this.$axios.delete(`/api/customschedule/id/${id}`)
        .then(response => {
          $.hideLoading();
          $("button[type='submit']").attr("disabled", false);
          
          if (response.data.success) {
            $.toptip('删除成功', 'success');
            
            if (detailListIndex !== undefined) {
              this.dataList[dataListIndex].detailScheduleList.splice(detailListIndex, 1);
              if (this.dataList[dataListIndex].detailScheduleList.length === 0) {
                setTimeout(() => {
                  $.closePopup();
                  window.location.reload();
                }, 1000);
              }
            } else {
              this.dataList.splice(dataListIndex, 1);
              setTimeout(() => {
                $.closePopup();
                window.location.reload();
              }, 1000);
            }
            
            this.hasDeleteAnyCustomSchedule = true;
          } else {
            $.toptip(response.data.message, 'error');
          }
        })
        .catch(error => {
          $.hideLoading();
          $("button[type='submit']").attr("disabled", false);
          
          if (error.response) {
            $.toptip(error.response.data.message, 'error');
          } else {
            $.toptip('网络连接异常，请检查并重试', 'error');
          }
        });
    },
    addCustomSchedule() {
      if (!this.customSchedule.scheduleName || this.customSchedule.scheduleName.length > 50) {
        this.errorMessage = "请正确填写课程名称";
        $("#error").show().delay(2000).hide(0);
      } else if (!this.customSchedule.scheduleLocation || this.customSchedule.scheduleLocation.length > 25) {
        this.errorMessage = "请正确填写上课地点";
        $("#error").show().delay(2000).hide(0);
      } else if (this.customSchedule.row === '' || this.customSchedule.column === '') {
        this.errorMessage = "请选择上课时间";
        $("#error").show().delay(2000).hide(0);
      } else if (this.customSchedule.scheduleLength === '') {
        this.errorMessage = "请选择上课时长";
        $("#error").show().delay(2000).hide(0);
      } else if (this.customSchedule.minScheduleWeek === '' || this.customSchedule.maxScheduleWeek === '') {
        this.errorMessage = "请选择上课周数";
        $("#error").show().delay(2000).hide(0);
      } else if (parseInt(this.customSchedule.minScheduleWeek) > parseInt(this.customSchedule.maxScheduleWeek)) {
        this.errorMessage = "初始上课周数不能大于结束上课周数";
        $("#error").show().delay(2000).hide(0);
      } else {
        $.showLoading("提交中");
        $("#submit").attr("disabled", true);
        
        this.$axios.post('/api/customshedule', {
          scheduleLength: parseInt(this.customSchedule.scheduleLength),
          scheduleName: this.customSchedule.scheduleName,
          scheduleLocation: this.customSchedule.scheduleLocation,
          position: parseInt(this.customSchedule.row) * 7 + parseInt(this.customSchedule.column),
          minScheduleWeek: parseInt(this.customSchedule.minScheduleWeek),
          maxScheduleWeek: parseInt(this.customSchedule.maxScheduleWeek)
        })
        .then(response => {
          $.hideLoading();
          $("#submit").attr("disabled", false);
          
          if (response.data.success) {
            $.toptip('提交成功', 'success');
            setTimeout(() => {
              $.closePopup();
              window.location.reload();
            }, 1000);
          } else {
            $.toptip(response.data.message, 'error');
          }
        })
        .catch(error => {
          $.hideLoading();
          $("#submit").attr("disabled", false);
          
          if (error.response) {
            $.toptip(error.response.data.message, 'error');
          } else {
            $.toptip('网络连接异常，请检查并重试', 'error');
          }
        });
      }
    },
    postQueryForm() {
      $.showLoading("加载中");
      let week = sessionStorage.getItem("scheduleWeek");
      
      let params = {};
      if (week) {
        params.week = week;
      }
      
      this.$axios.post('/api/schedulequery', params)
        .then(response => {
          $.hideLoading();
          
          if (response.data.success) {
            sessionStorage.setItem("scheduleWeek", response.data.data.week);
            this.currentWeek = "第" + response.data.data.week + "周";
            this.handleScheduleQueryResult(response.data.data);
          } else {
            $.alert({
              title: '查询课表信息失败',
              text: response.data.message
            });
          }
        })
        .catch(error => {
          $.hideLoading();
          
          if (error.response) {
            $.alert({
              title: '查询课表信息失败',
              text: error.response.data.message
            });
          } else {
            $.alert({
              title: '查询课表信息失败',
              text: '查询课表信息失败，请检查网络连接后重试'
            });
          }
        });
    },
    handleScheduleQueryResult(scheduleQueryResult) {
      let scheduleList = scheduleQueryResult.scheduleList;
      this.clearTableSchedule();
      
      if (scheduleList.length > 0) {
        scheduleList.sort((a, b) => a.position - b.position);
        
        let list = [];
        for (let i = 0; i < scheduleList.length; i++) {
          let item = {};
          let mergedItemMaxLength = scheduleList[i].scheduleLength;
          
          for (let j = i + 1; j < scheduleList.length; j++) {
            if (scheduleList[j].position === scheduleList[i].position) {
              if (j === i + 1) {
                item.detailScheduleList = [];
                item.merged = true;
                item.detailScheduleList.push(scheduleList[i]);
              }
              item.detailScheduleList.push(scheduleList[j]);
              if (scheduleList[j].scheduleLength > mergedItemMaxLength) {
                mergedItemMaxLength = scheduleList[j].scheduleLength;
              }
            } else {
              break;
            }
          }
          
          if (item.merged) {
            item.row = scheduleList[i].row;
            item.column = scheduleList[i].column;
            item.colorCode = scheduleList[i].colorCode;
            item.maxLength = mergedItemMaxLength;
            list.push(item);
            i = i + item.detailScheduleList.length;
          } else {
            scheduleList[i].merged = false;
            list.push(scheduleList[i]);
          }
        }
        
        list.forEach((item, index) => {
          item.index = index;
        });
        
        this.dataList = list;
      }
      
      $("#toast").show().delay(1000).hide(0);
    },
    showScheduleDetail(row, col) {
      let schedule = this.getScheduleByPosition(row - 1, col - 1);
      if (!schedule) return;
      
      this.isDialogOpen = true;
      
      if (schedule.merged) {
        this.currentMergedSchedule = schedule;
        $("#customScheduleDetailDialog").popup();
      } else {
        this.currentSchedule = schedule;
        $("#scheduleDetailDialog").popup();
      }
    },
    closeScheduleDetailDialog() {
      this.isDialogOpen = false;
      $.closePopup();
    },
    closeCustomScheduleDetailDialog() {
      this.isDialogOpen = false;
      $.closePopup();
      
      if (this.hasDeleteAnyCustomSchedule) {
        window.location.reload();
      }
    },
    clearTableSchedule() {
      this.dataList = [];
    },
    getScheduleByPosition(row, col) {
      return this.dataList.find(item => 
        item.row === row && item.column === col
      );
    },
    isCellHidden(row, col) {
      let schedule = this.getScheduleByPosition(row - 1, col - 1);
      if (!schedule) return false;
      
      if (schedule.merged) {
        return row !== schedule.row + 1;
      } else {
        return row !== schedule.row + 1;
      }
    },
    getCellRowspan(row, col) {
      let schedule = this.getScheduleByPosition(row - 1, col - 1);
      if (!schedule) return 1;
      
      return schedule.merged ? schedule.maxLength : schedule.scheduleLength;
    },
    getCellStyle(row, col) {
      let schedule = this.getScheduleByPosition(row - 1, col - 1);
      if (!schedule) return {};
      
      return {
        backgroundColor: schedule.colorCode
      };
    },
    getCellContent(row, col) {
      let schedule = this.getScheduleByPosition(row - 1, col - 1);
      if (!schedule) return '';
      
      if (schedule.merged) {
        return "该时间段存在多个课程，点击查看详情";
      } else {
        return `${schedule.scheduleName}@${schedule.scheduleLocation}`;
      }
    },
    getCellIndex(row, col) {
      let schedule = this.getScheduleByPosition(row - 1, col - 1);
      if (!schedule) return null;
      
      return schedule.index;
    },
    setTextColor() {
      let today = new Date().getDay();
      if (today === 0) {
        $("table th").eq(7).addClass("now");
      } else {
        $("table th").eq(today).addClass("now");
      }
    },
    refreshScheduleData() {
      $.showLoading("加载中");
      
      this.$axios.post('/api/refreshschedule')
        .then(response => {
          $.hideLoading();
          
          if (response.data.success) {
            this.postQueryForm();
          } else {
            $.alert({
              title: '刷新课表信息失败',
              text: response.data.message,
              onOK: () => {
                this.$router.go(-1);
              }
            });
          }
        })
        .catch(error => {
          $.hideLoading();
          
          if (error.response) {
            $.alert({
              title: '刷新课表信息失败',
              text: error.response.data.message,
              onOK: () => {
                this.$router.go(-1);
              }
            });
          } else {
            $.alert({
              title: '刷新课表信息失败',
              text: '网络连接异常，请检查并重试',
              onOK: () => {
                this.$router.go(-1);
              }
            });
          }
        });
    },
    showOptionMenu() {
      weui.actions({
        actions: [{
          text: "管理缓存配置",
          onClick: () => {
            window.location.href = '/privacy';
          }
        }, {
          text: "更新实时数据",
          onClick: () => {
            this.refreshScheduleData();
          }
        }, {
          text: "添加自定义课程",
          onClick: () => {
            this.showAddCustomScheduleDialog();
          }
        }]
      });
    }
  }
};
</script>

<style scoped>
@import '../../../static/css/css/schedule/schedule.css';
.hidden {
  display: none;
}
</style>