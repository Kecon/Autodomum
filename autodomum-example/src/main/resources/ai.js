var LAMP_DOWNSTAIRS = "1";
var LAMP_UPSTAIRS = "2";

function setLampState(eventContext, name, on) {
	var lamp = eventContext.getLampDao().getLamp(name);
	lamp.setOn(on);
	eventContext.getLampDao().updateLamp(lamp.getId(), lamp);
}

var TurnOffAllLamps = new Packages.com.autodomum.core.event.EventCallback() {
	work: function(eventContext, event) {
		var reset = true;
		if(event instanceof Java.type("com.autodomum.core.event.FireOnceEvent")) {
			if(event.getName().equals('turnOffAllLampsEvening')) {
				eventComponent.registerEventOnceTomorrow(0, eventContext.getRandom().nextInt(30), 'turnOffAllLampsEvening');				
			} else  if(event.getName().equals('turnOffAllLampsMorning')) {
				eventComponent.registerEventOnceTomorrow(8,eventContext.getRandom().nextInt(30), 'turnOffAllLampsMorning');				
			} else {
				reset = false;
			}
		}
		
		if(reset) {
			for each (var lamp in eventContext.getLampDao().getLamps()) {
				lamp.setOn(false);
				eventContext.getLampDao().updateLamp(lamp.getId(), lamp);
			}
		}
	}
}

var ChangeLampStateEvent = new Packages.com.autodomum.core.event.EventCallback() {
	work: function(eventContext, event) {
		switch(event.getName()) {
		case 'turnOnLampDownstairs':
			setLampState(eventContext, LAMP_DOWNSTAIRS, true);
			break;
			
		case 'turnOffLampDownstairs':
			setLampState(eventContext, LAMP_DOWNSTAIRS, false);
			break;

		case 'turnOnLampUpstairs':
			setLampState(eventContext, LAMP_UPSTAIRS, true);
			break;
			
		case 'turnOffLampUpstairs':
			setLampState(eventContext, LAMP_UPSTAIRS, false);
			break;
		}
	}
}

var TurnOnLampsEvening = new Packages.com.autodomum.core.event.EventCallback() {
	work: function(eventContext, event) {
			eventComponent.registerEventOnce(eventContext.getRandom().nextInt(900000), 'turnOnLampDownstairs');
			eventComponent.registerEventOnce(eventContext.getRandom().nextInt(900000), 'turnOnLampUpstairs');
			
			eventComponent.registerEventOnce(3 * 3600000 + eventContext.getRandom().nextInt(120000), 'turnOffLampDownstairs');
			eventComponent.registerEventOnce(3 * 3600000 + eventContext.getRandom().nextInt(120000), 'turnOffLampUpstairs');
	}
}

eventComponent.register(Packages.com.autodomum.core.event.FireOnceEvent.class, TurnOffAllLamps);
eventComponent.register(Packages.com.autodomum.core.event.FireOnceEvent.class, ChangeLampStateEvent);

eventComponent.register(Packages.com.autodomum.core.event.SunsetEvent.class, TurnOnLampsEvening);

eventComponent.registerEventOnceTomorrow(0, 30, 'turnOffAllLampsEvening');
eventComponent.registerEventOnceTomorrow(8, 30, 'turnOffAllLampsMorning');
