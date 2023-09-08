//
//  AppModule.swift
//  ios
//
//  Created by Harshith Shetty on 03/05/22.
//  Copyright © 2022 Harshith Shetty. All rights reserved.
//

import Foundation
import shared

class DiModule {
    static var koin = {
        KoinInit().doInit(
                appDeclaration: { _ in
                    // Do nothing
                }
        )
    }()
}
