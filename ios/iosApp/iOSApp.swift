import SwiftUI
import shared
import UIKit

@main
struct iOSApp: App {
    init() {
        DiModule.koin
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