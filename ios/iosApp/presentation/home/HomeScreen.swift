//
// Created by Joel Kanyi on 27/08/2023.
// Copyright (c) 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct HomeScreen: View {
    let homeViewmodel = DiModule.homeViewModel()
    var body: some View {
        VStack {
            Text(homeViewmodel.getHome())
        }
    }
}