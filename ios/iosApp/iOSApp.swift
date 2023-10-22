import SwiftUI
import shared
import UIKit

@main
struct iOSApp: App {
    init() {
        DiModule.koin
        requestNotificationPermission()
    }
	var body: some Scene {
		WindowGroup {
			ComposeView()
		}
	}
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> some UIViewController {
        // File name "Main" + "Kt" -> "Function Name"
        return MainKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {}
}

func requestNotificationPermission() {
    UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { success, error in
                if success {
                    print("Permission granted.")
                } else if let error = error {
                    print(error.localizedDescription)
                    // Displaying information to enable notifications in the future
                }
            }
}
