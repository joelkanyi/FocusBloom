import SwiftUI
import shared

struct ContentView: View {
	let greet = Greeting().greet()

	let homeViewmodel = DiModule.homeViewModel()
	let settingsViewmodel = DiModule.settingsViewModel()
	let statisticsViewmodel = DiModule.statisticsViewModel()
	let calendarViewmodel = DiModule.calendarViewModel()


	var body: some View {
	  MainScreen()
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
