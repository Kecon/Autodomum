function updateLamps() {
	jQuery.getJSON('lamps', function(data) {
		var html = '';
		jQuery.each(data, function(key, val) {
			var img;
			var onclick;
			if (val.on) {
				img = 'lampon.png';
				onclick = 'switchOff(\'' + val.id + '\')';
			} else {
				img = 'lampoff.png';
				onclick = 'switchOn(\'' + val.id + '\')';
			}

			html = html + '<img src="' + img + '" class="lamp" style="top:'
					+ val.y + 'px; left: ' + val.x + 'px;" onclick="'
					+ onclick + '" alt="' + val.name
					+ '" id="'
					+ val.id + '" />';

		});

		jQuery('#house').html(html);
	});
}

function switchOn(id) {
    $.ajax({
        type: 'post',
        url: 'lamps/' + id,
        data: JSON.stringify({on : true}),
        contentType: "application/json; charset=utf-8",
        traditional: true,
        success: function (data) {
        	updateLamps()
        }
    });		
}

function switchOff(id) {
    $.ajax({
        type: 'post',
        url: 'lamps/' + id,
        data: JSON.stringify({on : false}),
        contentType: "application/json; charset=utf-8",
        traditional: true,
        success: function (data) {
        	updateLamps()
        }
    });		
}