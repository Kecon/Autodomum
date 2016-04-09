var TestEvent = new Packages.com.autodomum.core.event.EventCallback() {
	work: function(eventContext, event) {
	}
}

eventComponent.register(Packages.com.autodomum.core.event.FireOnceEvent.class, TestEvent);