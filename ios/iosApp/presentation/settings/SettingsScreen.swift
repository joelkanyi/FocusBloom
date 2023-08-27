//
// Created by Joel Kanyi on 27/08/2023.
// Copyright (c) 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct SettingsScreen: View {
    let settingsViewmodel = DiModule.settingsViewModel()
    var body: some View {
        VStack {
            Text(settingsViewmodel.getSettings())
        }
    }
}