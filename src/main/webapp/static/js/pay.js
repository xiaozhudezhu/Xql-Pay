$(document)
		.ready(
				function() {
					$
							.get(
									'baseProduct/query',
									function(data) {
										var html = '';
										$
												.each(
														data,
														function(i, item) {
															html += '<li class="weui-wepay-pay-select__li">'
																	+ '<a href="javascript:;" class="weui-wepay-pay-select__item'
																	+ (i == 0 ? ' weui-wepay-pay-select__item_on'
																			: '')
																	+ '" productId="'
																	+ item.id
																	+ '" amount="'
																	+ item.amount
																	+ '">'
																	+ item.name
																	+ '</a>'
																	+ '</li>'
														});
										$('.pay-product').html(html);
										$(".weui-wepay-pay-select__li")
												.on(
														'click',
														function() {
															$(this)
																	.children()
																	.addClass(
																			"weui-wepay-pay-select__item_on");
															$(this)
																	.siblings()
																	.children()
																	.removeClass(
																			"weui-wepay-pay-select__item_on");
															return false;
														});
									});

					if (isWxBrowser()) {
						$('.pay-way li:eq(0)').hide();
						$('.pay-way a:eq(0)').removeClass(
								'weui-wepay-pay-select__item_on');
						$('.pay-way a:eq(1)').addClass(
								'weui-wepay-pay-select__item_on');
					}
					;

				});

function pay(payWay, payAmount, productId, productName) {
	var payWay = $('.pay-way .weui-wepay-pay-select__item_on').attr('payWay');
	var $productSelect = $('.pay-product .weui-wepay-pay-select__item_on');
	var payMethod = /Android|webOS|iPhone|iPod|BlackBerry/i
			.test(navigator.userAgent) ? 'wapPay' : 'pcPay';
	var totalAmount = $productSelect.attr('amount');
	var productName = $productSelect.text();
	var productId = $productSelect.attr('productId');
	if (payWay == 'wxpay') {
		wxPay(payWay, payMethod, totalAmount, productName, productId);
	} else if (payWay == 'alipay') {
		aliPay(payWay, payMethod, totalAmount, productName, productId);
	}
}

function isWxBrowser() {
	var ua = window.navigator.userAgent.toLowerCase();
	if (ua.match(/MicroMessenger/i) == 'micromessenger') {
		return true;
	} else {
		return false;
	}
}

/**
 * 支付宝支付
 * 
 * @param payWay
 * @param payMethod
 * @param totalAmount
 * @param productName
 * @param productId
 */
function aliPay(payWay, payMethod, totalAmount, productName, productId) {
	window.location.href = payWay + '/' + payMethod + '?productName='
			+ productName + '&totalAmount=' + totalAmount + '&productId='
			+ productId;
}

/**
 * 微信支付
 * 
 * @param payWay
 * @param payMethod
 * @param totalAmount
 * @param productName
 * @param productId
 */
function wxPay(payWay, payMethod, totalAmount, productName, productId) {
	if (payMethod == 'wapPay') {
		if(isWxBrowser())
			wxPayWeb(payWay, payMethod, totalAmount, productName, productId);
		else
			wxPayWap(payWay, payMethod, totalAmount, productName, productId);
	}
	else if (payMethod == 'pcPay')
		wxPayScanCode(payWay, payMethod, totalAmount, productName, productId);
}

/* 微信扫码支付 */
function wxPayScanCode(payWay, payMethod, totalAmount, productName, productId) {
	$.showLoading("正在加载...");
	$.post("wxpay/scanCode1", {
		productId : productId,
	}, function(res) {
		$.hideLoading();
		if (res.code == 0) {
			var name = res.data;
			console.log(name);
			wxShowScanCode('#qrcode1', name);
		} else {
			if (res.code == 2) {
				layer.alert(res.message);
			} else {
				layer.msg("error：" + res.message, {
					shift : 6
				});
			}
		}
	});

}

function wxShowScanCode(id, name) {
	$(id).attr('src', name);
	layer.open({
		type : 1,
		shade : true,
		title : "请打开微信客户端扫码支付",
		content : $('#qrcode1').parent()
	});
	
}
/* 微信扫码支付 END */
/* 微信公众号支付支付 */
function wxPayWeb(payWay, payMethod, totalAmount, productName, productId) {
	$.showLoading("正在加载...");
	$.post("wxpay/webPay", {
		totalAmount : totalAmount * 100,
		productName : productName,
		productId : productId
	}, function(res) {
		$.hideLoading();
		if (res.code == 0) {
			var data = $.parseJSON(res.data);

			if (typeof WeixinJSBridge == "undefined") {
				if (document.addEventListener) {
					document.addEventListener('WeixinJSBridgeReady',
							onBridgeReady(data), false);
				} else if (document.attachEvent) {
					document.attachEvent('WeixinJSBridgeReady',
							onBridgeReady(data));
					document.attachEvent('onWeixinJSBridgeReady',
							onBridgeReady(data));
				}
			} else {
				onBridgeReady(data);
			}
		} else {
			if (res.code == 2) {
				layer.alert(res.message);
			} else {
				layer.msg("error：" + res.message, {
					shift : 6
				});
			}
		}
	});

}

function onBridgeReady(json) {
	WeixinJSBridge.invoke('getBrandWCPayRequest', json, function(res) {
		// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回 ok，但并不保证它绝对可靠。
		if (res.err_msg == "get_brand_wcpay_request:ok") {
			layer.msg("支付成功", {
				shift : 6
			});

			self.location = "success";

		} else {
			layer.msg("支付失败", {
				shift : 6
			});
		}
	});
}
/* 微信公众号支付支付 END */

/**
 * 微信h5支付
 */
function wxPayWap(payWay, payMethod, totalAmount, productName, productId) {
	window.location.href = 'wxpay/wapPay' + '?productName='
			+ productName + '&totalAmount=' + (totalAmount * 100) + '&productId='
			+ productId;
}