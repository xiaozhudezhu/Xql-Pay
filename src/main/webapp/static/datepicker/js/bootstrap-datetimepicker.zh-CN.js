/**
 * Simplified Chinese translation for bootstrap-datetimepicker
 * Yuan Cheung <advanimal@gmail.com>
 */
;(function($){
	$.fn.datetimepicker.dates['zh-CN'] = {
				days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
			daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
			daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
			months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			today: "今日",
		suffix: [],
		meridiem: []
	};
	
	$.fn.datepicker=function(opt){
		var defaultOptions ={
			//两者配合使用
			//format:'yyyy-mm-dd hh:ii',
			//minView:'0',//分
			startView:'month',
			minView:'month',
			format:'yyyy-mm-dd',
			weekStart:1,
			autoclose:true,
			todayBtn:true,
			pickerPosition:'bottom-right',
			language:'zh-CN',
			pickerReferer :'span'
		};
		var formate={};
		switch(opt){
			case "hh:ii":
				formate={
					startView:'day',
					minView:'hour',
					format:'hh:ii'
				};
				break;
			case "hh:ii:ss":
				formate={
					startView:'day',
					minView:'hour',
					format:'hh:ii:ss'
				};
				break;
			case "yyyy-mm-dd hh:ii:ss.fff":
				formate={
					minView:'hour',
					format:'yyyy-mm-dd hh:ii:ss.fff'
				};
				break;
			case "yyyy-mm-dd hh:ii:ss":
				formate={
					minView:'hour',
					format:'yyyy-mm-dd hh:ii:ss'
				};
				break;
			case "yyyy-mm-dd hh:ii":
				formate={
					minView:'hour',
					format:'yyyy-mm-dd hh:ii'
				};
				break;
			case "yyyy-mm-dd hh":
				formate={
					minView:'day',
					format:'yyyy-mm-dd hh'
				};
				break;
			case "yyyy-mm":
				formate={
					startView:'year',
					minView:'year',
					format:'yyyy-mm'
				};
				break;
			case "yyyy":
				formate={
					startView:'decade',
					minView:'decade',
					format:'yyyy'
				};
				break;
		}
		$.extend(defaultOptions,formate);
		return this.datetimepicker(defaultOptions);
	};
}(jQuery));
